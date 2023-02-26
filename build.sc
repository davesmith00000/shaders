import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`
import mill._
import mill.scalalib._
import mill.scalajslib._
import mill.scalajslib.api._

import $ivy.`io.indigoengine::mill-indigo:0.14.1-SNAPSHOT`, millindigo._

import $ivy.`io.github.davidgregory084::mill-tpolecat::0.3.2`
import io.github.davidgregory084.TpolecatModule

object shaders extends mill.Module {

  object basics extends mill.Module {

    object colours extends ShaderModule { val title: String = "Colours" }
    object minimal extends ShaderModule { val title: String = "Minimal" }

  }

  object patterns extends mill.Module {

    object `simple-voronoi` extends ShaderModule { val title: String = "Simple Voronoi" }

  }

  def genSite(linkAll: Boolean = true) = T.command {
    // Extract all sub-projects
    val findProjects = os
      .proc(
        "mill",
        "resolve",
        "__.fastLinkJS"
      )
      .spawn(cwd = os.pwd)

    val filterOutTestProjects = os
      .proc(
        "grep",
        "-v",
        "test"
      )
      .spawn(cwd = os.pwd, stdin = findProjects.stdout)

    val cleanUpNames = os
      .proc(
        "sed",
        "s/.fastLinkJS//"
      )
      .spawn(cwd = os.pwd, stdin = filterOutTestProjects.stdout)

    val projectList =
      Stream.continually(cleanUpNames.stdout.readLine()).takeWhile(_ != null).toList

    // fullLinkJS all the shaders
    if(linkAll) {
      projectList.foreach { pjt =>
        os.proc("mill", s"$pjt.fullLinkJS").call(cwd = os.pwd)
      }
    }

    // Recreate the docs directory
    val docs = os.pwd / "docs"
    os.remove.all(docs)
    os.makeDir.all(docs)

    // Build a folder structure
    // Insert template HTML into each leaf
    // Copy the built JS scripts into each

    // Build an index page with links to all the sub folders
    os.write(docs / "index.html", HomePage.page(projectList))

    // Move it all into the docs/ folder.
  }

}

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

  object test extends Tests {
    def ivyDeps = Agg(
      ivy"org.scalameta::munit::0.7.29"
    )

    override def moduleKind = T(mill.scalajslib.api.ModuleKind.CommonJSModule)
  }

}

import $ivy.`com.lihaoyi::scalatags:0.8.2`

object HomePage {

  import scalatags.Text.all._

  def page(projectList: List[String]) =
    html(
      head(title := "Dave's Shader List")(
        meta(charset := "UTF-8"),
      ),
      body(
        projectList.map { prj =>
          p(prj)
        }
      )
    )

}

// Not cross compiled for Scala 2.13...
// import $ivy.`io.indigoengine::tyrian:0.6.1`

// object HomePage {

//   import tyrian.*
//   import tyrian.Html.*

//   def page(projectList: List[String]): Html[Nothing] =
//     html(
//       head(
//         meta(charset := "UTF-8"),
//         title("Dave's Shader List")
//       ),
//       body(
//         projectList.map { prj =>
//           p(prj)
//         }
//       )
//     )

// }
