import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`
import mill._
import mill.scalalib._
import mill.scalajslib._
import mill.scalajslib.api._

import $ivy.`io.indigoengine::mill-indigo:0.14.1-SNAPSHOT`, millindigo._
import $ivy.`io.github.davidgregory084::mill-tpolecat::0.3.0`

import io.github.davidgregory084.TpolecatModule

object shaders extends mill.Module {

  object patterns extends mill.Module {

    object `simple-voronoi` extends ScalaJSModule with MillIndigo with TpolecatModule {
      def scalaVersion   = "3.2.1"
      def scalaJSVersion = "1.13.0"

      val gameAssetsDirectory: os.Path   = os.pwd / "assets"
      val showCursor: Boolean            = true
      val title: String                  = "My Game - Made with Indigo"
      val windowStartWidth: Int          = 400
      val windowStartHeight: Int         = 400
      val disableFrameRateLimit: Boolean = false
      val backgroundColor: String        = "black"
      val electronInstall                = indigoplugin.ElectronInstall.Latest

      def buildGame() =
        T.command {
          T {
            compile()
            fastOpt()
            indigoBuild()()
          }
        }

      def runGame() =
        T.command {
          T {
            compile()
            fastOpt()
            indigoRun()()
          }
        }

      val indigoVersion = "0.14.1-SNAPSHOT"

      def ivyDeps =
        Agg(
          ivy"io.indigoengine::indigo-json-circe::$indigoVersion",
          ivy"io.indigoengine::indigo::$indigoVersion",
          ivy"io.indigoengine::indigo-extras::$indigoVersion"
        )

      def scalacOptions = T(super.scalacOptions().filterNot(Set("-migration", "-Xsource:3")))

      object test extends Tests {
        def ivyDeps = Agg(
          ivy"org.scalameta::munit::0.7.29"
        )

        override def moduleKind = T(mill.scalajslib.api.ModuleKind.CommonJSModule)

        def scalacOptions = T(super.scalacOptions().filterNot(Set("-migration", "-Xsource:3")))
      }

    }
  }
}
