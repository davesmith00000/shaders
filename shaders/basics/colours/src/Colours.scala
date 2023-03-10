import indigo.*

import scala.scalajs.js.annotation._

@JSExportTopLevel("IndigoGame")
object Colours extends IndigoShader:

  val config: GameConfig =
    GameConfig.default
      .withFrameRateLimit(FPS.`60`)
      .withViewport(400, 400)

  val assets: Set[AssetType]      = Set()
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

  // Based on the default shader from shadertoy
  @SuppressWarnings(Array("scalafix:DisableSyntax.var"))
  inline def fragment: Shader[FragmentEnv, Unit] =
    Shader[FragmentEnv] { env =>

      def fragment(color: vec4): vec4 =
        // Time varying pixel color
        val col: vec3 = 0.5f + 0.5f * cos(env.TIME + env.UV.xyx + vec3(0.0f, 2.0f, 4.0f))
        vec4(col, 1.0f)

    }
