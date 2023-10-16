import indigo.*

import scala.scalajs.js.annotation.*
import generated.*

@JSExportTopLevel("IndigoGame")
object CellularNoise extends IndigoShader:

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

      // https://github.com/ashima/webgl-noise/blob/master/src/cellular2D.glsl
      def mod289Vec3(x: vec3): vec3 =
        x - (floor(x * (1.0f / 289.0f)) * 289.0f)

      def mod289Vec2(x: vec2): vec2 =
        x - (floor(x * (1.0f / 289.0f)) * 289.0f)

      // Modulo 7 without a division
      def mod7(x: vec3): vec3 =
        x - (floor(x * (1.0f / 7.0f)) * 7.0f)

      // Permutation polynomial: (34x^2 + 6x) mod 289
      def permute(x: vec3): vec3 =
        mod289Vec3((34.0f * x + 10.0f) * x)

      // Cellular noise, returning F1 and F2 in a vec2.
      // Standard 3x3 search window for good F1 and F2 values
      def cellular(P: vec2): vec2 = {
        val K      = 0.142857142857f // 1/7
        val Ko     = 0.428571428571f // 3/7
        val jitter = 1.0f            // Less gives more regular pattern

        val Pi: vec2 = mod289Vec2(floor(P))
        val Pf: vec2 = fract(P)

        var oi: vec3 = vec3(-1.0, 0.0, 1.0)
        var of: vec3 = vec3(-0.5, 0.5, 1.5)
        var px: vec3 = permute(Pi.x + oi)
        var p: vec3  = permute(px.x + Pi.y + oi) // p11, p12, p13
        var ox: vec3 = fract(p * K) - Ko
        var oy: vec3 = mod7(floor(p * K)) * K - Ko
        var dx: vec3 = Pf.x + 0.5f + jitter * ox
        var dy: vec3 = Pf.y - of + jitter * oy
        var d1: vec3 = dx * dx + dy * dy         // d11, d12 and d13, squared

        p = permute(px.y + Pi.y + oi) // p21, p22, p23
        ox = fract(p * K) - Ko
        oy = mod7(floor(p * K)) * K - Ko
        dx = Pf.x - 0.5f + jitter * ox
        dy = Pf.y - of + jitter * oy
        var d2: vec3 = dx * dx + dy * dy // d21, d22 and d23, squared

        p = permute(px.z + Pi.y + oi) // p31, p32, p33
        ox = fract(p * K) - Ko
        oy = mod7(floor(p * K)) * K - Ko
        dx = Pf.x - 1.5f + jitter * ox
        dy = Pf.y - of + jitter * oy
        var d3: vec3 = dx * dx + dy * dy // d31, d32 and d33, squared

        // Sort out the two smallest distances (F1, F2)
        val d1a: vec3 = min(d1, d2)
        d2 = max(d1, d2)  // Swap to keep candidates for F2
        d2 = min(d2, d3)  // neither F1 nor F2 are now in d3
        d1 = min(d1a, d2) // F1 is now in d1
        d2 = max(d1a, d2) // Swap to keep candidates for F2

        val d1Flip1 = if (d1.x < d1.y) d1 else vec3(d1.y, d1.x, d1.z) // Swap if smaller
        val d1Flip2 =
          if (d1Flip1.x < d1Flip1.z) d1Flip1
          else vec3(d1Flip1.z, d1Flip1.y, d1Flip1.x) // F1 is in d1.x

        var d1Flip3: vec3 = vec3(d1Flip2.x, min(d1Flip2.yz, d2.yz)) // F2 is now not in d2.yz
        d1Flip3 = vec3(d1Flip3.x, min(d1Flip3.y, d1Flip3.z), d1Flip3.z) // nor in  d1Flip3.z
        d1Flip3 =
          vec3(d1Flip3.x, min(d1Flip3.y, d2.x), d1Flip3.z) // F2 is in d1Flip3.y, we're done.

        sqrt(d1Flip3.xy)
      }

      def fragment(color: vec4): vec4 =
        vec4(cellular(env.UV * 8.0f), 0.0f, 1.0f)

    }
