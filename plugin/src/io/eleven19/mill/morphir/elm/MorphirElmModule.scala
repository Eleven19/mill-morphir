package io.eleven19.mill.morphir.elm

import io.eleven19.mill.morphir.MorphirModule
import mill._
import mill.api.Task

trait MorphirElmModule extends ElmModule with MorphirModule {
    def morphirElmCommand: Task[String] = Task("morphir-elm")
    def useFallbackCli:    Boolean      = false

    override def makeCommandArgs(cliCommand: String, destPath: os.Path): Task[Seq[String]] = Task.Anon {
        val args                = super.makeCommandArgs(cliCommand, destPath)()
        val shouldUseMorphirElm = cliCommand.endsWith("morphir-elm")
        if (shouldUseMorphirElm && useFallbackCli) {
            args ++ Seq("--fallback-cli")
        } else {
            args
        }
    }
}
