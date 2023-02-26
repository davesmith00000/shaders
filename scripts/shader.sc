import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`
import mill._
import mill.scalalib._
import mill.scalajslib._
import mill.scalajslib.api._

import $ivy.`io.indigoengine::mill-indigo:0.14.1-SNAPSHOT`, millindigo._

import $ivy.`io.github.davidgregory084::mill-tpolecat::0.3.2`
import io.github.davidgregory084.TpolecatModule

trait ShaderModule extends ScalaJSModule with MillIndigo with TpolecatModule {
  val title: String

  def scalaVersion   = "3.2.1"
  def scalaJSVersion = "1.13.0"

  val gameAssetsDirectory: os.Path   = os.pwd / "assets"
  val showCursor: Boolean            = true
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

  def buildGameFull() =
    T.command {
      T {
        compile()
        fullOpt()
        indigoBuildFull()()
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

  def runGameFull() =
    T.command {
      T {
        compile()
        fullOpt()
        indigoRunFull()()
      }
    }

  val indigoVersion = "0.14.1-SNAPSHOT"

  def ivyDeps =
    Agg(
      ivy"io.indigoengine::indigo-json-circe::$indigoVersion",
      ivy"io.indigoengine::indigo::$indigoVersion",
      ivy"io.indigoengine::indigo-extras::$indigoVersion"
    )

  object test extends Tests {
    def ivyDeps = Agg(
      ivy"org.scalameta::munit::0.7.29"
    )

    override def moduleKind = T(mill.scalajslib.api.ModuleKind.CommonJSModule)
  }

}
