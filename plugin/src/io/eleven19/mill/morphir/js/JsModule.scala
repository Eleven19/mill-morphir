package io.eleven19.mill.morphir.js

import mill._
import mill.api.Task
import mill.scalalib._

trait JsModule extends Module {
    def artifactName:           Task[String] = Task(moduleDir.last)
    def jsPackageManagerRunner: Task[String] = Task("npx")
    def jsPackageManagerCmd:    Task[String] = Task("npm")

    /**
     * The folders containing all source files fed into the compiler
     */
    def allSources: Task[Seq[PathRef]] = Task(sources() ++ generatedSources())

    /**
     * Folders containing source files that are generated rather than
     * hand-written; these files can be generated in this target itself, or can
     * refer to files generated from other targets
     */
    def generatedSources: Task[Seq[PathRef]] = Task(Seq.empty[PathRef])
    def sources = Task.Sources(moduleDir / "src")

    /**
     * All individual source files fed into the compiler/tooling.
     */
    def allSourceFiles: Task[Seq[PathRef]] = Task {
        Lib.findSourceFiles(allSources(), Seq("js", "ts")).map(PathRef(_))
    }

}
