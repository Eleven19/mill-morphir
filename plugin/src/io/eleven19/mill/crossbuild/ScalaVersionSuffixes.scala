package io.eleven19.mill.crossbuild

import io.eleven19.mill.crossbuild.VersionString

trait ScalaVersionSuffixes[A] {
    def versionSuffixes(input: A): Seq[String]
}
object ScalaVersionSuffixes {
    def apply[A](
        implicit ev: ScalaVersionSuffixes[A]
      ): ScalaVersionSuffixes[A] = ev

    implicit val stringScalaVersionSuffixes: ScalaVersionSuffixes[String] =
        new ScalaVersionSuffixes[String] {
            def versionSuffixes(input: String): Seq[String] =
                VersionString.majorMinorParts(input) match {
                    case Some((3, _))  => Seq("2.12+", "2.13+")
                    case Some((2, 13)) => Seq("2", "2.12+", "2.13+", "2.12-2.13")
                    case Some((2, 12)) => Seq("2", "2.12+", "2.11-2.12", "2.12-2.13")
                    case Some((2, 11)) => Seq("2", "2.11-2.12")
                    case _             => Seq.empty
                }
        }

    implicit val versionStringScalaVersionSuffixes: ScalaVersionSuffixes[VersionString] =
        new ScalaVersionSuffixes[VersionString] {
            def versionSuffixes(input: VersionString): Seq[String] =
                input.majorMinorParts match {
                    case Some((3, _))  => Seq("2.12+", "2.13+")
                    case Some((2, 13)) => Seq("2", "2.12+", "2.13+", "2.12-2.13")
                    case Some((2, 12)) => Seq("2", "2.12+", "2.11-2.12", "2.12-2.13")
                    case Some((2, 11)) => Seq("2", "2.11-2.12")
                    case _             => Seq.empty
                }
        }
}
