import mill.scalalib.publish._
import mill.scalalib.scalafmt._
import $ivy.`io.chris-kipp::mill-ci-release::0.1.9`
import $ivy.`com.github.lolgab::mill-crossplatform::0.2.3`
import mill._, mill.scalalib._, mill.scalajslib._, mill.scalanativelib._
import com.github.lolgab.mill.crossplatform._
import io.kipp.mill.ci.release._

trait MyPublishModule extends CiReleaseModule {
    override def pomSettings = PomSettings(
        description = "Scala based test kits for Morphir",
        organization = "io.github.eleven19",
        url = "https://github.com/eleven19/scala-morphir-testkits",
        licenses = Seq(License.`Apache-2.0`),
        versionControl = VersionControl.github(owner="eleven19", repo="scala-morphir-testkits"),
        developers = Seq(
            Developer("DamianReeves", "Damian Reeves", "https://github.com/DamianReeves")
        )
    )

    override def sonatypeHost: Option[SonatypeHost] = Some(SonatypeHost.s01)

    override def artifactName: T[String] = T {
        val name = super.artifactName()
        s"morphir-$name"
    }
}

trait CommonNative extends ScalaNativeModule {
    def scalaNativeVersion = "0.4.14"
}

trait CommonJS extends ScalaJSModule {
    def scalaJSVersion = "1.13.1"
}

val scalaVersions = Seq("2.13.11", "3.3.0")

object testkit extends Cross[TestkitModule](scalaVersions)
trait TestkitModule extends CrossPlatform {
    trait Shared extends CrossPlatformCrossScalaModule with MyPublishModule with ScalafmtModule
    object jvm extends Shared 
    object js extends Shared with CommonJS
    object native extends Shared with CommonNative
}