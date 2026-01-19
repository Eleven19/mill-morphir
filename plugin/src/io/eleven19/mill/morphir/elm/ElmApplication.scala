package io.eleven19.mill.morphir.elm

import mill._
import mill.api.Task

trait ElmApplication extends ElmModule {
    def targetFileName = Task("elm.js")

    def elmMake: Task[PathRef] = Task {
        val moduleName = moduleDir.last
        val destPath   = Task.dest / targetFileName()

        val commandArgs = Seq(
            jsPackageManagerRunner(),
            "elm",
            "make",
            "--output",
            destPath.toString(),
        ) ++ allSourceFiles().map(_.path.toString)
        os.proc(commandArgs).call(cwd = Task.ctx().workspace, env = Task.ctx().env)
        PathRef(destPath)
    }
}
