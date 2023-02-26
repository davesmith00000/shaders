import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`
import mill._
import mill.scalalib._
import mill.scalajslib._
import mill.scalajslib.api._

import $file.scripts.shader
import $file.scripts.templates
import $file.scripts.gensite

object shaders extends mill.Module {

  def genSite(linkAll: Boolean = true) = T.command {
    gensite.make(linkAll)
  }

  object basics extends mill.Module {

    object colours extends shader.ShaderModule { val title: String = "Colours" }
    object minimal extends shader.ShaderModule { val title: String = "Minimal" }

  }

  object patterns extends mill.Module {

    object `simple-voronoi` extends shader.ShaderModule { val title: String = "Simple Voronoi" }

  }

}