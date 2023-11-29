import indigo.*

import scala.scalajs.js.annotation.*
import generated.*

/** Make a top down view of a doughnut, where the gray value is essentially a height map of a
  * doughtnut shape. Then turn it into a normal map.
  */

@JSExportTopLevel("IndigoGame")
object DoughnutNormal extends IndigoShader:

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

  inline def fragment: Shader[FragmentEnv, Unit] =
    Shader[FragmentEnv] { env =>

      def sdCircle(p: vec2, r: Float): Float =
        length(p) - r

      def rateOfChange(d: Float, m: Float): Float =
        val c = (cos((d - 0.25f) * env.PI * m) * 0.5f) + 0.5f
        c * min(step(-2.0f / m, d), 1.0f - step(2.0f / m, d))

      def fragment(color: vec4): vec4 =
        val p   = env.UV - 0.5f
        val sdf = sdCircle(p, 0.25f)

        val normalised = normalize(p)
        var d          = if sdf < 0.0f then -normalised else normalised
        d = clamp(d, vec2(-1.0f), vec2(1.0f))
        d = (d + 1.0f) * 0.5f

        val curve = clamp(rateOfChange(sdf, 20.0f), 0.0f, 1.0f)

        val normalColor = vec4(d, 1.0f, 1.0f)
        val baseColor   = vec4(0.5f, 0.5f, 1.0f, 1.0f)

        mix(baseColor, normalColor, curve)
    }
