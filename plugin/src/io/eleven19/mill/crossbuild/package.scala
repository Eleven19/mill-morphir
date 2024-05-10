package io.eleven19.mill

import upickle.default.{ReadWriter => RW, macroRW}
package object crossbuild {
    final case class VersionString(version: String) extends AnyVal {
        def parts:      Seq[Int]         = version.split('.').map(_.toInt)
        def maybeParts: Seq[Option[Int]] = version.split('.').map(_.toIntOption)

        def majorMinorParts: Option[(Int, Int)] = maybeParts.take(2) match {
            case Seq(Some(major), Some(minor), _*) => Some((major, minor))
            case Seq(Some(major))                  => Some((major, 0))
            case _                                 => None
        }
    }

    object VersionString {
        implicit val rw: RW[VersionString] = macroRW

        def parts(version:           String): Seq[Int]           = VersionString(version).parts
        def maybeParts(version:      String): Seq[Option[Int]]   = VersionString(version).maybeParts
        def majorMinorParts(version: String): Option[(Int, Int)] = VersionString(version).majorMinorParts

        object Major {
            def unapply(version: VersionString): Option[Int] = version.maybeParts.head
        }

        object MajorMinor {
            def unapply(version: VersionString): Option[(Int, Int)] = version.majorMinorParts
        }
    }
}
