import indigo.*

import scala.scalajs.js.annotation.*
import generated.*

@JSExportTopLevel("IndigoGame")
object SimplexNoise extends IndigoShader:

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

      // https://github.com/ashima/webgl-noise/blob/master/src/noise2D.glsl
      def mod289Vec3(x: vec3): vec3 =
        x - floor(x * (1.0f / 289.0f)) * 289.0f

      def mod289Vec2(x: vec2): vec2 =
        x - floor(x * (1.0f / 289.0f)) * 289.0f

      def permute(x: vec3): vec3 =
        mod289Vec3(((x * 34.0f) + 10.0f) * x)

      val C: vec4 = vec4(
        0.211324865405187,  // (3.0-sqrt(3.0))/6.0
        0.366025403784439,  // 0.5*(sqrt(3.0)-1.0)
        -0.577350269189626, // -1.0 + 2.0 * C.x
        0.024390243902439   // 1.0 / 41.0
      )

      def snoise(v: vec2): Float =
        // First corner
        var i: vec2  = floor(v + dot(v, C.yy))
        val x0: vec2 = v - i + dot(i, C.xx)

        // Other corners
        // var i1: vec2 = null
        // i1.x = step( x0.y, x0.x ) // x0.x > x0.y ? 1.0 : 0.0
        // i1.y = 1.0 - i1.x
        val i1: vec2 = if x0.x > x0.y then vec2(1.0, 0.0) else vec2(0.0, 1.0)
        // x0 = x0 - 0.0 + 0.0 * C.xx
        // x1 = x0 - i1 + 1.0 * C.xx
        // x2 = x0 - 1.0 + 2.0 * C.xx
        var x12: vec4 = x0.xyxy + C.xxzz
        x12 = vec4(x12.xy - i1, x12.zw)

        // Permutations
        i = mod289Vec2(i) // Avoid truncation effects in permutation
        val p: vec3 = permute(permute(i.y + vec3(0.0f, i1.y, 1.0f)) + i.x + vec3(0.0f, i1.x, 1.0f))

        var m: vec3 = max(0.5f - vec3(dot(x0, x0), dot(x12.xy, x12.xy), dot(x12.zw, x12.zw)), 0.0f)
        m = m * m
        m = m * m

        // Gradients: 41 points uniformly over a line, mapped onto a diamond.
        // The ring size 17*17 = 289 is close to a multiple of 41 (41*7 = 287)

        val x: vec3  = 2.0f * fract(p * C.www) - 1.0f
        val h: vec3  = abs(x) - 0.5f
        val ox: vec3 = floor(x + 0.5f)
        val a0: vec3 = x - ox

        // Normalise gradients implicitly by scaling m
        // Approximation of: m *= inversesqrt( a0*a0 + h*h )
        m *= 1.79284291400159f - 0.85373472095314f * (a0 * a0 + h * h)

        // Compute final noise value at P
        var g: vec3 =
          vec3(
            a0.x * x0.x + h.x * x0.y,
            a0.yz * x12.xz + h.yz * x12.yw
          )

        130.0f * dot(m, g)

      def fragment(color: vec4): vec4 =
        val n = (snoise(env.UV) * 0.25f) +
          (snoise(env.UV * 2.0f) * 0.25f) +
          (snoise(env.UV * 8.0f) * 0.25f) +
          (snoise(env.UV * 32.0f) * 0.25f)
        vec4(vec3(n), 1.0f)
    }
