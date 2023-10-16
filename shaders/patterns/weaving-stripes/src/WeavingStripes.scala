import indigo.*

import scala.scalajs.js.annotation.*
import generated.*

@JSExportTopLevel("IndigoGame")
object WeavingStripes extends IndigoShader:

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

      def fragment(color: vec4): vec4 =

        val offset: Float  = sin(env.UV.x * env.SIZE.x / 8.0f) * 0.03f
        val offsetA: Float = 0.25f + offset
        val offsetB: Float = 0.25f - offset

        val multiple = 16.0f

        // Graphtoy: step(0.5,clamp(sin(x*PI),0,0.5))
        val yPosA: Float = (env.UV.y + offsetA) * multiple
        val amountA: Float = step(
          0.5f,
          clamp(sin(yPosA * env.PI), 0.0f, 0.5f)
        )

        // Graphtoy: step(0.5,clamp(sin(PI+x*PI),0,0.5))
        val yPosB: Float = (env.UV.y + offsetB) * multiple
        val amountB: Float = step(
          0.5f,
          clamp(sin(env.PI + yPosB * env.PI), 0.0f, 0.5f)
        )

        val c: vec3 = vec3(amountA, amountB, 0.0f)

        vec4(c, 1.0f)

    }
