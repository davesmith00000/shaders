import indigo.*

import scala.scalajs.js.annotation._

@JSExportTopLevel("IndigoGame")
object Minimal extends IndigoShader:

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

  @SuppressWarnings(Array("scalafix:DisableSyntax.var"))
  inline def fragment: Shader[FragmentEnv, Unit] =
    Shader[FragmentEnv] { env =>
      def fragment(color: vec4): vec4 =
        vec4(1.0f, 0.0f, 0.0f, 1.0f)
    }
