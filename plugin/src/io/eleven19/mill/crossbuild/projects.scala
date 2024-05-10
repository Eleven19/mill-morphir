package io.eleven19.mill.crossbuild

import mill._
import mill.scalajslib._
import mill.scalalib._
import mill.scalalib.api.ZincWorkerUtil
import mill.scalanativelib._
import os.Path

/// A platform-aware project that is built for a specific Scala version but possibly for multiple platforms (i.e. JVM, JS, Native)
trait PlatformAwareScalaProject extends PlatformScalaModule with PlatformAwareScalaModule {}

/// A platform-aware project that is cross-built for multiple Scala versions and possibly for multiple platforms (i.e. JVM, JS, Native)
trait PlatformAwareCrossScalaProject extends PlatformScalaModule with PlatformAwareCrossScalaModule {}

trait ScalaJvmProject extends PlatformAwareScalaProject {
    def platform: Platform = Platform.JVM
}

trait ScalaJsProject extends PlatformAwareScalaProject with ScalaJSModule {
    def platform: Platform = Platform.JS
}

trait ScalaNativeProject extends PlatformAwareScalaProject with ScalaNativeModule {
    def platform: Platform = Platform.Native
}

trait CrossScalaJvmProject extends PlatformAwareCrossScalaProject {
    def platform: Platform = Platform.JVM
}

trait CrossScalaJsProject extends PlatformAwareCrossScalaProject with ScalaJSModule {
    def platform: Platform = Platform.JS
}

trait CrossScalaNativeProject extends PlatformAwareCrossScalaProject with ScalaNativeModule {
    def platform: Platform = Platform.Native
}
