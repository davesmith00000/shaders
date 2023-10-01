import indigo.*

import scala.scalajs.js.annotation.*
import noise.whitenoise.*

@JSExportTopLevel("IndigoGame")
object WhiteNoise extends IndigoShader:

  val config: GameConfig =
    ShaderConfig.config

  val assets: Set[AssetType]      = ShaderAssets.assets.assetSet
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

  // Ported from: https://www.youtube.com/watch?v=l-07BXzNdPw&feature=youtu.be
  @SuppressWarnings(Array("scalafix:DisableSyntax.var"))
  inline def fragment: Shader[FragmentEnv, Unit] =
    Shader[FragmentEnv] { env =>

      def whiteNoise(p: vec2): vec3 =
        var a: vec3 = fract(p.xyx * vec3(123.34f, 234.34f, 345.65f))
        a = a + dot(a, a + 34.45f)
        fract(vec3(a.x * a.y, a.y * a.z, a.z * a.x))

      def fragment(color: vec4): vec4 =
        vec4(whiteNoise(env.UV + fract(env.TIME)), 1.0f)

    }
