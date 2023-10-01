import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`
import mill._
import mill.scalalib._
import mill.scalajslib._
import mill.scalajslib.api._

import $file.scripts.shader
import $file.scripts.templates
import $file.scripts.gensite

import indigoplugin._

object shaders extends mill.Module {

  def genSite(linkAll: Boolean = true) = T.command {
    gensite.make(linkAll)
  }

  object basics extends mill.Module {

    object colours extends shader.ShaderModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("Colours")

      val indigoGenerators: IndigoGenerators =
        makeIndigoGenerators("basics.colors")
    }
    object minimal extends shader.ShaderModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("Minimal")

      val indigoGenerators: IndigoGenerators =
        makeIndigoGenerators("basics.minimal")
    }

  }

  object demos extends mill.Module {

    object campfire extends shader.ShaderModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("Campfire")
          .withWindowSize(192, 192)

      val indigoGenerators: IndigoGenerators =
        makeIndigoGenerators("demos.campfire")
    }

    object pulsingbox extends shader.ShaderModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("PulsingBox")

      val indigoGenerators: IndigoGenerators =
        makeIndigoGenerators("demos.pulsingbox")
    }

  }

  object noise extends mill.Module {

    object `white-noise` extends shader.ShaderModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("White Noise")

      val indigoGenerators: IndigoGenerators =
        makeIndigoGenerators("noise.whitenoise")
    }

    object `gradient-noise` extends shader.ShaderModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("Gradient Noise")

      val indigoGenerators: IndigoGenerators =
        makeIndigoGenerators("noise.gradientnoise")
    }

  }

  object patterns extends mill.Module {

    object `simple-voronoi` extends shader.ShaderModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("Simple Voronoi")

      val indigoGenerators: IndigoGenerators =
        makeIndigoGenerators("patterns.simplevoronoi")
    }

  }

  object sdf extends mill.Module {

    object circle extends shader.ShaderModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("Circle SDF")

      val indigoGenerators: IndigoGenerators =
        makeIndigoGenerators("sdf.circle")
    }

    object square extends shader.ShaderModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("Square SDF")

      val indigoGenerators: IndigoGenerators =
        makeIndigoGenerators("sdf.square")
    }

  }

}
