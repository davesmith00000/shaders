import indigo.*

import scala.scalajs.js.annotation.*
import generated.*

@JSExportTopLevel("IndigoGame")
object ClassicPerlinNoise extends IndigoShader:

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

      // This is based on the classic variant (not the periodic)
      // https://github.com/ashima/webgl-noise/blob/master/src/classicnoise2D.glsl
      def mod289(x: vec4): vec4 =
        x - floor(x * (1.0f / 289.0f)) * 289.0f

      def permute(x: vec4): vec4 =
        mod289(((x * 34.0f) + 10.0f) * x)

      def taylorInvSqrt(r: vec4): vec4 =
        1.79284291400159f - 0.85373472095314f * r

      def fade(t: vec2): vec2 =
        t * t * t * (t * (t * 6.0f - 15.0f) + 10.0f)

      // Classic Perlin noise
      def cnoise(P: vec2): Float =
        var Pi: vec4 = floor(P.xyxy) + vec4(0.0, 0.0, 1.0, 1.0)
        val Pf: vec4 = fract(P.xyxy) - vec4(0.0, 0.0, 1.0, 1.0)
        Pi = mod289(Pi) // To avoid truncation effects in permutation

        val ix: vec4 = Pi.xzxz
        val iy: vec4 = Pi.yyww
        val fx: vec4 = Pf.xzxz
        val fy: vec4 = Pf.yyww

        val i: vec4 = permute(permute(ix) + iy)

        var gx: vec4 = fract(i * (1.0f / 41.0f)) * 2.0f - 1.0f
        val gy: vec4 = abs(gx) - 0.5f
        val tx: vec4 = floor(gx + 0.5f)
        gx = gx - tx

        var g00: vec2 = vec2(gx.x, gy.x)
        var g10: vec2 = vec2(gx.y, gy.y)
        var g01: vec2 = vec2(gx.z, gy.z)
        var g11: vec2 = vec2(gx.w, gy.w)

        val norm: vec4 =
          taylorInvSqrt(vec4(dot(g00, g00), dot(g01, g01), dot(g10, g10), dot(g11, g11)))
        g00 *= norm.x
        g01 *= norm.y
        g10 *= norm.z
        g11 *= norm.w

        val n00: Float = dot(g00, vec2(fx.x, fy.x))
        val n10: Float = dot(g10, vec2(fx.y, fy.y))
        val n01: Float = dot(g01, vec2(fx.z, fy.z))
        val n11: Float = dot(g11, vec2(fx.w, fy.w))

        val fade_xy: vec2 = fade(Pf.xy)
        val n_x: vec2     = mix(vec2(n00, n01), vec2(n10, n11), fade_xy.x)
        val n_xy: Float   = mix(n_x.x, n_x.y, fade_xy.y)

        2.3f * n_xy

      def fragment(color: vec4): vec4 =
        vec4(vec3(cnoise(env.UV * 8.0f)), 1.0f)

    }
