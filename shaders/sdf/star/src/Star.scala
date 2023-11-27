import indigo.*

import scala.scalajs.js.annotation.*
import generated.*

@JSExportTopLevel("IndigoGame")
object Star extends IndigoShader:

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
  // https://www.shadertoy.com/view/3tSGDy
  inline def fragment: Shader[FragmentEnv, Unit] =
    Shader[FragmentEnv] { env =>

      /** Calculate the distance of the point p from the star with radius r, and a relative value rf
        * controlling the depth of the stars points.
        */
      def sdStar5(p: vec2, r: Float, rf: Float): Float =
        @const val k1: vec2 = vec2(0.809016994375f, -0.587785252292f)
        @const val k2: vec2 = vec2(-k1.x, k1.y)

        var p2 = vec2(abs(p.x), p.y)

        p2 = p2 - 2.0f * max(dot(k1, p2), 0.0f) * k1
        p2 = p2 - 2.0f * max(dot(k2, p2), 0.0f) * k2
        p2 = vec2(abs(p2.x), p2.y - r)

        val ba: vec2 = rf * vec2(-k1.y, k1.x) - vec2(0.0f, 1.0f)
        val h: Float = clamp(dot(p2, ba) / dot(ba, ba), 0.0f, r)

        length(p2 - ba * h) * sign(p2.y * ba.x - p2.x * ba.y)

      def fragment(color: vec4): vec4 =

        // The calculation assumes we're centered on the origin.
        val sdf = sdStar5(env.UV - 0.5f, 0.25f, 0.6f)

        // Inside the square is a negative value, so we flip it, and use step to get a hard edge.
        val col = step(0.0f, -sdf)

        // Output as a grey scale.
        vec4(vec3(col), 1.0f)
    }
