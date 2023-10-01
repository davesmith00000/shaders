import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`
import mill._
import mill.scalalib._
import mill.scalajslib._
import mill.scalajslib.api._

import $file.scripts.shadermodule
import $file.scripts.templates
import $file.scripts.gensite

import indigoplugin._

object shaders extends mill.Module {

  def genSite(linkAll: Boolean = true) = T.command {
    gensite.make(linkAll)
  }

  object basics extends mill.Module {

    object colours extends shadermodule.ShaderModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("Colours")

      val indigoGenerators: IndigoGenerators =
        makeIndigoGenerators("generated", "ColoursConfig", "ColoursAssets")
    }
    object minimal extends shadermodule.ShaderModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("Minimal")

      val indigoGenerators: IndigoGenerators =
        makeIndigoGenerators("generated", "MinimalConfig", "MinimalAssets")
    }

  }

  object demos extends mill.Module {

    object campfire extends shadermodule.ShaderModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("Campfire")
          .withWindowSize(192, 192)

      val indigoGenerators: IndigoGenerators =
        makeIndigoGenerators("generated", "CampfireConfig", "CampfireAssets")
    }

    object pulsingbox extends shadermodule.ShaderModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("PulsingBox")

      val indigoGenerators: IndigoGenerators =
        makeIndigoGenerators("generated", "PulsingBoxConfig", "PulsingBoxAssets")
    }

  }

  object noise extends mill.Module {

    object `cellular-noise` extends shadermodule.ShaderModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("Cellular Noise")

      val indigoGenerators: IndigoGenerators =
        makeIndigoGenerators("generated", "CellularNoiseConfig", "CellularNoiseAssets")
    }

    object `gradient-noise` extends shadermodule.ShaderModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("Gradient Noise")

      val indigoGenerators: IndigoGenerators =
        makeIndigoGenerators("generated", "GradientNoiseConfig", "GradientNoiseAssets")
    }

    object `white-noise` extends shadermodule.ShaderModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("White Noise")

      val indigoGenerators: IndigoGenerators =
        makeIndigoGenerators("generated", "WhiteNoiseConfig", "WhiteNoiseAssets")
    }

  }

  object patterns extends mill.Module {

    object `simple-voronoi` extends shadermodule.ShaderModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("Simple Voronoi")

      val indigoGenerators: IndigoGenerators =
        makeIndigoGenerators("generated", "SimpleVoronoiConfig", "SimpleVoronoiAssets")
    }

  }

  object sdf extends mill.Module {

    object circle extends shadermodule.ShaderModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("Circle SDF")

      val indigoGenerators: IndigoGenerators =
        makeIndigoGenerators("generated", "CircleSDFConfig", "CircleSDFAssets")
    }

    object square extends shadermodule.ShaderModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("Square SDF")

      val indigoGenerators: IndigoGenerators =
        makeIndigoGenerators("generated", "SquareSDFConfig", "SquareSDFAssets")
    }

  }

}
