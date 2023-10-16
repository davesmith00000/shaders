import indigo.*

import scala.scalajs.js.annotation.*
import generated.*

@JSExportTopLevel("IndigoGame")
object SquareSDF extends IndigoShader:

  val config: GameConfig =
    Config.config.noResize

  val assets: Set[AssetType]      = Assets.assets.assetSet
  val channel0: Option[AssetPath] = None
  val channel1: Option[AssetPath] = None
  val channel2: Option[AssetPath] = None
  val channel3: Option[AssetPath] = None

  val shader: Shader =
    CustomShader.shader

object CustomShader:

  val shader: Shader =
    UltravioletShader.entityFragment(
      ShaderId("shader"),
      EntityShader.fragment[FragmentEnv](fragment, FragmentEnv.reference)
    )

  import ultraviolet.syntax.*

  // Based on work by Inigo Quilez
  // https://iquilezles.org/articles/distfunctions2d/
  inline def fragment: Shader[FragmentEnv, Unit] =
    Shader[FragmentEnv] { env =>

      // Calculate the distance of the point p from the box edge b.
      def sdBox(p: vec2, b: vec2): Float =
        val d = abs(p) - b
        length(max(d, 0.0f)) + min(max(d.x, d.y), 0.0f)

      def fragment(color: vec4): vec4 =

        // The calculation assumes we're centered on the origin.
        val sdf = sdBox(env.UV - 0.5f, vec2(0.25f))

        // Inside the square is a negative value, so we flip it, and use step to get a hard edge.
        val col = step(0.0f, -sdf)

        // Output as a grey scale.
        vec4(vec3(col), 1.0f)
    }
