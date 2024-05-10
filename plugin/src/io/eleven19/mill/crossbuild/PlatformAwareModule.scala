package io.eleven19.mill.crossbuild

import io.eleven19.mill.crossbuild.VersionString
import mill._
import mill.scalajslib._
import mill.scalalib._
import mill.scalalib.api.ZincWorkerUtil
import mill.scalanativelib._
import os.Path

trait PlatformAwareModule extends JavaModule { self =>

    override def compileModuleDeps =
        super.compileModuleDeps ++ platformCompileModuleDeps.flatMap(_.childPlatformModules(platform))

    def knownPlatforms: T[Seq[Platform]] = T(Seq(platform))

    override def moduleDeps = {
        val platform = self.platform
        super.moduleDeps ++ platformModuleDeps.flatMap { case p => p.childPlatformModules(platform) }
    }

    def crossPlatformSourceSuffixes(
        version:                        Option[ItemVersion],
        srcFolderName:                  String,
        versionedDirectoryNamesEnabled: VersionedDirectoryNamesEnabled,
      ) =
        for {
            versionSuffix  <- versionSuffixes(version, versionedDirectoryNamesEnabled)
            platformSuffix <- platformSuffixes()
        } yield (versionSuffix, platformSuffix) match {
            case ("", "")     => srcFolderName
            case ("", suffix) => s"$srcFolderName-$suffix"
            case (suffix, "") => s"$srcFolderName-$suffix"
            case (vs, ps)     => s"$srcFolderName-$vs-$ps"
        }

    def crossPlatformRelativeSourcePaths(
        version:                        Option[ItemVersion],
        srcFolderName:                  String,
        versionedDirectoryNamesEnabled: VersionedDirectoryNamesEnabled,
      ): Seq[os.RelPath] =
        for {
            versionSuffix  <- versionSuffixes(version, versionedDirectoryNamesEnabled)
            platformSuffix <- platformSuffixes()
        } yield (versionSuffix, platformSuffix) match {
            case ("", "")     => os.rel / srcFolderName
            case ("", suffix) => os.rel / suffix / srcFolderName
            case (suffix, "") => os.rel / s"$srcFolderName-$suffix"
            case (vs, ps)     => os.rel / ps / s"$srcFolderName-$vs"
        }

    def crossPlatformSources: T[Seq[PathRef]] = T.sources {
        val versions = itemVersions().toList
        val versionsResolved: Seq[Option[ItemVersion]] = versions.isEmpty match {
            case true  => Seq(None)
            case false => versions.map(v => Some(v))
        }
        val folderMode              = platformFolderMode()
        val versionedDirectoryNames = versionedDirectoryNamesEnabled()

        (for {
            version <- versionsResolved
        } yield {
            folderMode match {
                case Platform.FolderMode.UseSuffix =>
                    crossPlatformSourceSuffixes(version, "src", versionedDirectoryNames).map(suffix =>
                        PathRef(millSourcePath / suffix)
                    )
                case Platform.FolderMode.UseNesting =>
                    crossPlatformRelativeSourcePaths(version, "src", versionedDirectoryNames).map(subPath =>
                        PathRef(millSourcePath / subPath)
                    )
                case Platform.FolderMode.UseBoth =>
                    (crossPlatformSourceSuffixes(version, "src", versionedDirectoryNames).map(suffix =>
                        PathRef(millSourcePath / suffix)
                    ) ++
                        crossPlatformRelativeSourcePaths(version, "src", versionedDirectoryNames).map(subPath =>
                            PathRef(millSourcePath / subPath)
                        )).distinct
            }
        }).flatten.distinct
    }

    def itemVersions: T[Seq[ItemVersion]] = T {
        Seq.empty[ItemVersion]
    }

    def partialVersion(version: String): Option[(Int, Int)] = {
        val partial = version.split('.').take(2)
        for {
            major    <- partial.headOption
            majorInt <- major.toIntOption
            minor    <- partial.lastOption
            minorInt <- minor.toIntOption
        } yield (majorInt, minorInt)
    }

    def platformModuleDeps:        Seq[CrossPlatform] = Seq.empty
    def platformCompileModuleDeps: Seq[CrossPlatform] = Seq.empty

    def platformFolderMode: T[Platform.FolderMode] = T(Platform.FolderMode.UseNesting)
    def platform:           Platform

    def platformSuffixes(): Seq[String] = Seq("") ++ platform.suffixes

    def resolvedPlatform: T[Platform] = T(platform)

    def versionedDirectoryNamesEnabled: T[VersionedDirectoryNamesEnabled] = T(VersionedDirectoryNamesEnabled.Disabled)

    protected def versionedDirectoryNames(
        version:                        ItemVersion,
        versionedDirectoryNamesEnabled: VersionedDirectoryNamesEnabled,
      ): Seq[String] =
        if (versionedDirectoryNamesEnabled.enabled) ZincWorkerUtil.matchingVersions(version.version)
        else Seq.empty

    def versionSuffixes(version: Option[ItemVersion], versionedDirectoryNamesEnabled: VersionedDirectoryNamesEnabled)
        : Seq[String] =
        Seq("") ++ version.map(v => versionedDirectoryNames(v, versionedDirectoryNamesEnabled)).getOrElse(Seq.empty)

    override def sources: mill.define.Target[Seq[mill.api.PathRef]] = T.sources {
        crossPlatformSources()
    }
}

trait PlatformAwareScalaModule extends ScalaModule with PlatformAwareModule {

    def extraScalaVersionSuffixes(scalaVersion: String): Seq[String] =
        partialVersion(scalaVersion) match {
            case Some((3, _))  => Seq("2.12+", "2.13+")
            case Some((2, 13)) => Seq("2", "2.12+", "2.13+", "2.12-2.13")
            case Some((2, 12)) => Seq("2", "2.12+", "2.11-2.12", "2.12-2.13")
            case Some((2, 11)) => Seq("2", "2.11-2.12")
            case _             => Seq.empty
        }

    override def itemVersions: T[Seq[ItemVersion]] = T {
        Seq(ItemVersion("scala", ItemVersionType.LanguageVersion, scalaVersion()))
    }

    override def versionSuffixes(
        version:                        Option[ItemVersion],
        versionedDirectoryNamesEnabled: VersionedDirectoryNamesEnabled,
      ): Seq[String] = (versionedDirectoryNamesEnabled, version) match {
        case (
                VersionedDirectoryNamesEnabled.Enabled,
                Some(ItemVersion("scala", ItemVersionType.LanguageVersion, scalaVersion)),
            ) =>
            super.versionSuffixes(version, versionedDirectoryNamesEnabled) ++ extraScalaVersionSuffixes(scalaVersion)
        case _ =>
            super.versionSuffixes(version, versionedDirectoryNamesEnabled)
    }
}

trait PlatformAwareCrossScalaModule extends CrossScalaModule with PlatformAwareScalaModule {
    override def versionedDirectoryNamesEnabled: T[VersionedDirectoryNamesEnabled] =
        T(VersionedDirectoryNamesEnabled.Enabled)
    override def sources: mill.define.Target[Seq[mill.api.PathRef]] = T.sources {
        crossPlatformSources()
    }
}
