package io.eleven19.mill.morphir

import io.eleven19.mill.morphir.api.{MakeArgs, MakeResult}
import mill._
import mill.scalalib._


trait MorphirModule extends Module { self =>
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

    def makeArgs: Task[MakeArgs] = T.task {
        MakeArgs(
            projectDir = morphirProjectDir().path,
            output = T.dest / morphirIrFilename(),
            indentJson = indentJson(),
            typesOnly = typesOnly(),
            fallbackCli = None
        )
    }

    def morphirMake: Target[MakeResult] = T {
        val destFolder = T.dest
        val makeArgs: MakeArgs = self.makeArgs()
        val cli = makeCommandRunner()
        val commandArgs = makeArgs.toCommandArgs(cli)
        val workingDir = makeArgs.projectDir
        val destPath = makeArgs.output
        util.Jvm.runSubprocess(commandArgs, T.ctx().env, workingDir)
        MakeResult(makeArgs, PathRef(destPath), commandArgs, workingDir)
    }

    /// Only include type information in the IR, no values.
    def typesOnly: Target[Boolean] = T { false }

}