import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`
import mill._
import mill.scalalib._
import mill.scalajslib._
import mill.scalajslib.api._

def make(linkAll: Boolean) = {
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

  // Build all the shaders
  if (linkAll) {
    projectList.foreach { pjt =>
      os.proc("mill", s"$pjt.buildGameFull").call(cwd = os.pwd)
    }
  }

  // Recreate the docs directory
  val docs = os.pwd / "docs"
  os.remove.all(docs)
  os.makeDir.all(docs)

  // Generate relative paths
  val projectListRelPaths: List[os.RelPath] =
    projectList.map { p =>
      os.RelPath(p.replaceAllLiterally(".", "/"))
    }

  // Copy all the built shaders into the right docs directory
  projectListRelPaths.foreach { p =>
    val outPath = (os.pwd / "docs") / p
    os.makeDir.all(outPath)

    val buildDir = os.pwd / "out" / p / "indigoBuildFull.dest"

    os.list(buildDir).toList.foreach { p =>
      os.copy(p, outPath / p.last)
    }
  }

  // Build an index page with links to all the sub folders
  os.write(docs / "index.html", templates.HomePage.page(projectListRelPaths.map(_.toString())))
}
