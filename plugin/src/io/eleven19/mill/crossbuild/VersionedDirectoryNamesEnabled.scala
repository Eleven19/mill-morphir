package io.eleven19.mill.crossbuild
import upickle.default.{ReadWriter => RW, macroRW}

sealed trait VersionedDirectoryNamesEnabled { self =>
    def enabled: Boolean = self match {
        case VersionedDirectoryNamesEnabled.Enabled  => true
        case VersionedDirectoryNamesEnabled.Disabled => false
    }

    def whenEnabled[T](f: => Any): Unit =
        if (enabled) f
}

object VersionedDirectoryNamesEnabled {
    implicit val rw: RW[VersionedDirectoryNamesEnabled] = RW.merge(
        macroRW[VersionedDirectoryNamesEnabled.Enabled],
        macroRW[VersionedDirectoryNamesEnabled.Disabled],
    )

    type Enabled = VersionedDirectoryNamesEnabled.Enabled.type
    case object Enabled extends VersionedDirectoryNamesEnabled {
        implicit val rw: RW[Enabled.type] = macroRW
    }

    type Disabled = VersionedDirectoryNamesEnabled.Disabled.type
    case object Disabled extends VersionedDirectoryNamesEnabled {
        implicit val rw: RW[Disabled.type] = macroRW
    }
}
