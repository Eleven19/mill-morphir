import $file.plugins
import mill._
import mill.define.Command
import mill.scalalib._
import mill.scalajslib._
import mill.scalanativelib._
import io.eleven19.mill.crossbuild._

object app extends CrossPlatform {
    trait Shared extends PlatformAwareScalaProject {
        def scalaVersion       = "3.3.3"
        def platformModuleDeps = Seq(lib)
    }
    object jvm extends ScalaJvmProject with Shared
    object js extends ScalaJsProject with Shared {
        def scalaJSVersion = "1.16.0"
    }

    // object native extends ScalaNativeProject with Shared {
    //     def scalaNativeVersion = "0.5.1"
    // }
}

object lib extends CrossPlatform {
    trait Shared extends PlatformAwareScalaProject {
        def scalaVersion                   = "3.3.3"
        def versionedDirectoryNamesEnabled = VersionedDirectoryNamesEnabled.Enabled
    }
    object jvm extends ScalaJvmProject with Shared
    object js extends ScalaJsProject with Shared {
        def scalaJSVersion = "1.16.0"
    }

    // object native extends ScalaNativeProject with Shared {
    //     def scalaNativeVersion = "0.5.1"
    // }
}

def verify(): Command[Unit] = T.command {
    app.jvm.allSources()
    app.jvm.compile()

    app.js.allSources()
    app.js.compile()

    // app.native.allSources()
    // app.native.compile()
    ()
}
