import indigo.*

import scala.scalajs.js.annotation.*
import generated.*

@JSExportTopLevel("IndigoGame")
object Campfire extends IndigoShader:

  val config: GameConfig =
    Config.config.noResize

  val assets: Set[AssetType]      = Assets.assets.assetSet
  val channel0: Option[AssetPath] = Option(AssetPath("assets/fire-background.png"))
  val channel1: Option[AssetPath] = Option(AssetPath("assets/campfire.png"))
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

      // -- noise: https://www.iquilezles.org/www/articles/gradientnoise/gradientnoise.htm --
      def hash(x: vec2): vec2 =
        val k = vec2(0.3183099f, 0.3678794f)
        val y = x * k + k.yx
        -1.0f + 2.0f * fract(16.0f * k * fract(y.x * y.y * (y.x + y.y)))

      def calcNoise(p: vec2): vec3 =
        val i: vec2  = floor(p)
        val f: vec2  = fract(p)
        val u: vec2  = f * f * (3.0f - 2.0f * f)
        val du: vec2 = 6.0f * f * (1.0f - f)
        val ga: vec2 = hash(i + vec2(0.0f, 0.0f))
        val gb: vec2 = hash(i + vec2(1.0f, 0.0f))
        val gc: vec2 = hash(i + vec2(0.0f, 1.0f))
        val gd: vec2 = hash(i + vec2(1.0f, 1.0f))

        val va: Float = dot(ga, f - vec2(0.0f, 0.0f))
        val vb: Float = dot(gb, f - vec2(1.0f, 0.0f))
        val vc: Float = dot(gc, f - vec2(0.0f, 1.0f))
        val vd: Float = dot(gd, f - vec2(1.0f, 1.0f))

        vec3(
          va + u.x * (vb - va) + u.y * (vc - va) + u.x * u.y * (va - vb - vc + vd),
          ga + u.x * (gb - ga) + u.y * (gc - ga) + u.x * u.y * (ga - gb - gc + gd) +
            du * (u.yx * (va - vb - vc + vd) + vec2(vb, vc) - va)
        )
      // -- / noise --

      // SDF circle https://www.iquilezles.org/www/articles/distfunctions2d/distfunctions2d.htm
      def sdfCircle(p: vec2, r: Float): Float =
        length(p) - r
      // SDF

      def fire(): vec4 =
        val octaves: Float      = 13.0f
        val timeMultiple: Float = 7.0f

        val noiseAmount: Float =
          calcNoise(vec2(octaves * env.UV.x, (octaves * env.UV.y) + (env.TIME * timeMultiple))).x

        val yGradient: Float = clamp(0.7f - env.UV.y, 0.0f, 1.0f) * 0.4f
        val sdfNoise: vec2   = vec2(noiseAmount * 0.1f, noiseAmount * 2.5f * yGradient)

        val yOffset = -0.18f

        val p1: vec2 = (env.UV - vec2(0.5f, 0.7f + yOffset)) + sdfNoise
        val p2: vec2 = (env.UV - vec2(0.5f, 0.775f + yOffset)) + sdfNoise
        val p3: vec2 = (env.UV - vec2(0.5f, 0.8f + yOffset)) + sdfNoise

        val scaleFactor = 0.48f

        val amountOuter: Float  = step(sdfCircle(p1, 0.25f * scaleFactor), 0.0f)
        val amountInner: Float  = step(sdfCircle(p2, 0.175f * scaleFactor), 0.0f)
        val amountCenter: Float = step(sdfCircle(p3, 0.1f * scaleFactor), 0.0f)

        val outer: vec3  = vec3(1.0f, 0.5f, 0.0f) * amountOuter
        val inner: vec3  = vec3(1.0f, 0.8f, 0.0f) * amountInner
        val center: vec3 = vec3(1.0f, 1.0f, 1.0f) * amountCenter

        vec4(outer + inner + center, amountOuter)

      def fragment(color: vec4): vec4 =
        val fireColor = fire()
        val bgColor   = env.CHANNEL_0
        val fgColor   = env.CHANNEL_1

        mix(mix(bgColor, fireColor, fireColor.a), fgColor, fgColor.a)
    }
