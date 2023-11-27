import indigo.*

import scala.scalajs.js.annotation.*
import generated.*

/** Make a top down view of a doughnut, where the gray value is essentially a height map of a
  * doughtnut shape. Then turn it into a normal map.
  */

@JSExportTopLevel("IndigoGame")
object Doughnut extends IndigoShader:

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

      def heightValue(d: Float, m: Float): Float =
        ((cos(d * env.PI * m) * 0.5f) + 0.5f) * min(step(-1.0f / m, d), 1.0f - step(1.0f / m, d))

      def sdCircle(p: vec2, r: Float): Float =
        length(p) - r

      def toNormal(direction: vec2, sdf: Float, height: Float): vec4 =
        val n = vec2(0.5) + direction * height
        vec4(n, 1.0f, 1.0f)

      def fragment(color: vec4): vec4 =

        val sdf    = sdCircle(env.UV - 0.5f, 0.25f)
        val height = heightValue(sdf, 10.0f)

        val col = toNormal(env.UV - 0.5f, sdf, height)

        // vec4(vec3(height), 1.0f) // return this to see the height version of the doughnut
        col
    }
