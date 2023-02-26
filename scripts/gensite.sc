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

  // fullLinkJS all the shaders
  if (linkAll) {
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
  os.write(docs / "index.html", templates.HomePage.page(projectList))

  // Move it all into the docs/ folder.
}
