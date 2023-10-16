import indigo.*

import scala.scalajs.js.annotation.*
import generated.*

@JSExportTopLevel("IndigoGame")
object GradientNoise extends IndigoShader:

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

      // https://www.iquilezles.org/www/articles/gradientnoise/gradientnoise.htm
      def hash(x: vec2): vec2 =
        val k = vec2(0.3183099f, 0.3678794f)
        val y = x * k + k.yx
        -1.0f + 2.0f * fract(16.0f * k * fract(y.x * y.y * (y.x + y.y)))

      def noise(p: vec2): vec3 =
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

      def fragment(color: vec4): vec4 =
        vec4(noise(env.UV * 8.0f), 1.0f)

    }
