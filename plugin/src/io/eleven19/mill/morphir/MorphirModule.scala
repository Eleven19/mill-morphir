package io.eleven19.mill.morphir

import io.eleven19.mill.morphir.api.{MakeArgs, MakeResult, MorphirProjectConfig}
import mill._
import mill.api.Task
import mill.api.JsonFormatters._
import mill.scalalib._
import upickle.default._

trait MorphirModule extends Module { self =>
    /// Use indentation in the generated JSON file.
    /// Use indentation in the generated JSON file.
    def indentJson: Task[Boolean] = Task(false)

    def morphirCommand: Task[String] = Task {
        val cmdCandidates = Seq("morphir-elm", "morphir")
        // Check for local node_modules binaries first
        val potentialBases = Seq(
            Task.ctx().workspace,
            moduleDir,
            moduleDir / os.up, // In case module is nested one level deep
        ).distinct

        val ways = potentialBases.flatMap { base =>
            val binPaths   = cmdCandidates.map(cmd => base / "node_modules" / ".bin" / cmd)
            val scriptPath = base / "node_modules" / "morphir-elm" / "cli" / "morphir-elm.js"
            binPaths :+ scriptPath
        }

        ways.find(os.exists).map(_.toString()).getOrElse("morphir-elm")
    }
    def morphirProjectDir: Task[PathRef] = Task.Source(moduleDir)

    def morphirProjectConfig: Task[MorphirProjectConfig] = Task {
        val morphirProjectFile = morphirProjectDir().path / "morphir.json"
        if (os.exists(morphirProjectFile)) {
            read[MorphirProjectConfig](os.read(morphirProjectFile), trace = true)
        } else {
            throw new Exception(s"morphir.json file not found, looked for it at ${morphirProjectFile}.")
        }
    }

    def makeCommandRunner: Task[String] = Task(morphirCommand())

    def morphirIrFilename = Task("morphir-ir.json")

    def makeCommandArgs(cliCommand: String, destPath: os.Path): Task[Seq[String]] = Task.Anon {
        Seq(
            "make",
            "--output",
            destPath.toString(),
            if (indentJson()) "--indent-json" else "",
            if (typesOnly()) "--types-only" else "",
        )
    }

    def makeArgs: Task[MakeArgs] = Task.Anon {
        MakeArgs(
            projectDir = morphirProjectDir().path,
            output = Task.dest / morphirIrFilename(),
            indentJson = indentJson(),
            typesOnly = typesOnly(),
            fallbackCli = None,
        )
    }

    def morphirMake: Task[MakeResult] = Task {
        val makeArgs: MakeArgs = self.makeArgs()
        val cli = makeCommandRunner()

        val commandArgs = if (cli.endsWith(".js") || cli.endsWith("morphir-elm")) {
            // It's a JS script, we need to run it with node
            Seq("node", cli) ++ makeArgs.toCommandArgs
        } else {
            makeArgs.toCommandArgs(cli)
        }

        val workingDir = makeArgs.projectDir
        val destPath   = makeArgs.output
        Task.ctx().log.error(s"DEBUG: Running command: $commandArgs in $workingDir")
        os.proc(commandArgs).call(cwd = workingDir, env = Task.ctx().env)
        MakeResult(makeArgs, PathRef(destPath), commandArgs, workingDir)
    }

    /// Only include type information in the IR, no values.
    def typesOnly: Task[Boolean] = Task(false)

}
