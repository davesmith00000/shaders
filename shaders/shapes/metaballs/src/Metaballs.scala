import indigo.*

import scala.scalajs.js.annotation.*
import generated.*

@JSExportTopLevel("IndigoGame")
object Metaballs extends IndigoShader:

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

  @SuppressWarnings(Array("scalafix:DisableSyntax.var"))
  inline def fragment: Shader[FragmentEnv, Unit] =
    Shader[FragmentEnv] { env =>

      def N22(p: vec2): vec2 =
        var a: vec3 = fract(p.xyx * vec3(123.34f, 234.34f, 345.65f))
        a = a + dot(a, a + 34.45f)
        fract(vec2(a.x * a.y, a.y * a.z))

      def fragment(color: vec4): vec4 =
        val uv: vec2 = (2.0f * env.SCREEN_COORDS - env.SIZE) / env.SIZE.y

        var acc: Float = 0.0f
        val count      = 20.0f

        _for(1.0f, _ < count, _ + 1.0f) { i =>
          val p: vec2      = sin(N22(vec2(i)) * env.TIME)
          val distance     = length(uv - p)
          val blobStrength = 10.0f
          val radius       = 0.1f
          val influence    = blobStrength * exp(-distance * distance / (2.0f * radius * radius))

          acc = clamp(acc + (influence / count), 0.0f, 1.0f)
        }

        vec4(vec3(smoothstep(acc, 0.0f, 0.05f)), 1.0f)

    }
