package io.eleven19.mill.crossbuild
import upickle.default.{ReadWriter => RW, macroRW}

final case class ItemVersion(
    itemName:        String,
    ItemVersionType: ItemVersionType,
    version:         String,
  )

object ItemVersion {
    implicit val rw: RW[ItemVersion] = macroRW

    def scalaVersion(version:   String) = ItemVersion("scala", ItemVersionType.LanguageVersion, version)
    def scalaJSVersion(version: String) = ItemVersion("scala-js", ItemVersionType.LanguageDialectVersion, version)
    def scalaNativeVersion(version: String) =
        ItemVersion("scala-native", ItemVersionType.LanguageDialectVersion, version)
}

sealed trait ItemVersionType
object ItemVersionType {
    implicit val rw: RW[ItemVersionType] = RW.merge(
        macroRW[ItemVersionType.LanguageVersion],
        macroRW[ItemVersionType.LanguageDialectVersion],
        macroRW[ItemVersionType.CustomType],
    )

    type LanguageVersion = ItemVersionType.LanguageVersion.type
    case object LanguageVersion extends ItemVersionType

    type LanguageDialectVersion = ItemVersionType.LanguageDialectVersion.type
    case object LanguageDialectVersion extends ItemVersionType

    case class CustomType(name: String) extends ItemVersionType
}
