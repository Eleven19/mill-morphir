package io.eleven19.mill.morphir.elm
import io.eleven19.mill.morphir.js.JsModule
import mill._
import mill.api.Task
import mill.scalalib._

trait ElmModule extends JsModule {

    /**
     * All individual source files fed into the Elm or Morphir elm compiler.
     */
    override def allSourceFiles: Task[Seq[PathRef]] = Task {
        Lib.findSourceFiles(allSources(), Seq("elm")).map(PathRef(_))
    }
}
