import mill._
import mill.scalalib._
import mill.scalajslib._
import mill.scalajslib.api._

import $ivy.`io.indigoengine::mill-indigo:0.15.2`, millindigo._

import $ivy.`io.github.davidgregory084::mill-tpolecat::0.3.5`
import io.github.davidgregory084.TpolecatModule

trait ShaderModule extends MillIndigo with TpolecatModule {
  def scalaVersion   = "3.3.1"
  def scalaJSVersion = "1.14.0"

  def indigoOptions: IndigoOptions

  def makeIndigoOptions(title: String): IndigoOptions =
    IndigoOptions.defaults
      .withTitle(title)
      .withWindowSize(400, 400)
      .withAssetDirectory(os.RelPath.rel / "assets")
      .withBackgroundColor("black")

  def indigoGenerators: IndigoGenerators =
    IndigoGenerators("generated")
      .generateConfig("Config", indigoOptions)
      .listAssets("Assets", indigoOptions.assets)

  def buildGame() =
    T.command {
      T {
        compile()
        fastLinkJS()
        indigoBuild()()
      }
    }

  def buildGameFull() =
    T.command {
      T {
        compile()
        fullLinkJS()
        indigoBuildFull()()
      }
    }

  def runGame() =
    T.command {
      T {
        compile()
        fastLinkJS()
        indigoRun()()
      }
    }

  def runGameFull() =
    T.command {
      T {
        compile()
        fullLinkJS()
        indigoRunFull()()
      }
    }

  val indigoVersion = "0.15.2"

  def ivyDeps =
    Agg(
      ivy"io.indigoengine::indigo-json-circe::$indigoVersion",
      ivy"io.indigoengine::indigo::$indigoVersion",
      ivy"io.indigoengine::indigo-extras::$indigoVersion"
    )

  object test extends ScalaJSTests {
    def ivyDeps = Agg(
      ivy"org.scalameta::munit::0.7.29"
    )

    override def moduleKind = T(mill.scalajslib.api.ModuleKind.CommonJSModule)

    def testFramework: mill.T[String] = T("munit.Framework")
  }

}
