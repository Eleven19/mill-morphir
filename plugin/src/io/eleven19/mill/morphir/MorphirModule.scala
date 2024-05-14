package io.eleven19.mill.morphir

import io.eleven19.mill.morphir.api.{MakeArgs, MakeResult}
import mill._
import mill.scalalib._


trait MorphirModule extends Module {
    /// Use indentation in the generated JSON file.
    def indentJson: Target[Boolean] = T { false }

    def morphirCommand: Target[String] = T { "morphir" }
    def morphirProjectDir: Target[PathRef] = T { PathRef(millSourcePath) }

    def makeCommandRunner: Target[String] = T { morphirCommand() }

    def morphirIrFilename = T("morphir-ir.json")

    def makeCommandArgs(cliCommand:String, destPath: os.Path): Task[Seq[String]] = T.task{
        Seq(
            "make",
            "--output",
            destPath.toString(),
            if (indentJson()) "--indent-json" else "",
            if (typesOnly()) "--types-only" else ""
        )
    }

    def morphirMake: Target[MakeResult] = T {
        val destPath = T.dest / morphirIrFilename()
        val cli = makeCommandRunner()
        val commandArgs = makeCommandArgs(cli, destPath)()
        util.Jvm.runSubprocess(commandArgs, T.ctx().env, morphirProjectDir().path)
        MakeResult(PathRef(destPath), commandArgs, morphirProjectDir().path)
    }

    /// Only include type information in the IR, no values.
    def typesOnly: Target[Boolean] = T { false }

}