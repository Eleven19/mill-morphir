package io.eleven19.mill.morphir.api

import mill.PathRef
import mill.api.JsonFormatters._

case class MakeResult( irFilePath: PathRef, commandArgs:Seq[String], workingDir: os.Path)
object MakeResult {
    implicit val jsonFormatter:upickle.default.ReadWriter[MakeResult] = upickle.default.macroRW
}

