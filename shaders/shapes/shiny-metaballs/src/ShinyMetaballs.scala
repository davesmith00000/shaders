import indigo.*

import scala.scalajs.js.annotation.*
import generated.*

@JSExportTopLevel("IndigoGame")
object ShinyMetaballs extends IndigoShader:

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

        var accColor: vec3  = vec3(0.0f)
        var accAlpha: Float = 0.0f
        var accDir: vec2    = vec2(0.0)
        var accInf: Float   = 0.0f
        val count           = 20.0f
        val red             = vec3(1.0f, 0.0f, 0.0f)
        val blue            = vec3(0.0f, 0.0f, 1.0f)

        _for(1.0f, _ < count, _ + 1.0f) { i =>
          val start        = N22(vec2(i))
          val p: vec2      = sin(start * env.TIME)
          val direction    = p - uv
          val distance     = length(uv - p)
          val blobStrength = 100.0f
          val radius       = 0.08f
          val influence    = blobStrength * exp(-distance * distance / (2.0f * radius * radius))

          val c = mix(red, blue, step(0.6f, fract(start.x)))

          accColor = accColor + (c * influence)
          accAlpha = clamp(accAlpha + (influence / count), 0.0f, 1.0f)
          accDir = accDir + ((direction * influence) / count)
          accInf = accInf + (influence / count)
        }

        val newAlpha = step(0.5f, accAlpha)
        val newColor = accColor / (accColor.r + accColor.g + accColor.b)

        val lightDir        = vec2(-1.0f, 1.0f)
        val newDirection    = accDir
        val height          = exp2(cos(accInf / count))
        val normal          = newDirection * height
        val shineAmount     = smoothstep(0.1f, 0.9f, dot(normal, lightDir))
        val highlightAmount = smoothstep(0.7f, 0.8f, dot(normal, lightDir))
        val shadowAmount =
          1.0f - (smoothstep(0.1f, 0.9f, clamp(dot(normal, -lightDir), 0.0f, 1.0f)) * 0.5f)

        val composedColour = (newColor * shadowAmount) + shineAmount + highlightAmount
        vec4(composedColour * newAlpha, newAlpha)

    }
