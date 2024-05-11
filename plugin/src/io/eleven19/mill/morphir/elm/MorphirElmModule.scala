package io.eleven19.mill.morphir.elm

import io.eleven19.mill.morphir.MorphirModule
import mill.{PathRef, T, Target, util}

trait MorphirElmModule extends ElmModule with MorphirModule{
    def morphirElmCommand: Target[String] = T { "morphir-elm" }
    override def makeCommandRunner: Target[String] = T { morphirElmCommand() }
}
