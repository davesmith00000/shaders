import indigo.*

import scala.scalajs.js.annotation.*
import generated.*

@JSExportTopLevel("IndigoGame")
object PulsingBox extends IndigoShader:

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

  inline def fragment: Shader[FragmentEnv, Unit] =
    Shader[FragmentEnv] { env =>

      def sdBox(p: vec2, b: vec2): Float =
        val d = abs(p) - b
        length(max(d, 0.0f)) + min(max(d.x, d.y), 0.0f)

      def makeBox(time: Float, minDistance: Float, distMultiplier: Float): Float =
        val distance = vec2(minDistance + abs(sin(time) * distMultiplier))
        sdBox(env.UV - 0.5f, distance)

      def makeFrame(
          time: Float,
          halfStrokeWidth: Float,
          halfGlowWidth: Float,
          minDistance: Float,
          distMultiplier: Float
      ): vec4 =

        val sdf = makeBox(time, minDistance, distMultiplier)

        val frame = 1.0f - step(0.0f, abs(-sdf) - halfStrokeWidth)
        val col   = vec3(0.0f, frame, frame)

        val glowAmount = smoothstep(0.97f, 1.05f, 1.0f - (abs(sdf) - halfGlowWidth))
        val glow       = vec3(0.0f, glowAmount * 0.5f, glowAmount * 0.5f)

        val alpha = glowAmount + frame

        vec4((glow + col) * alpha, alpha)

      def fragment(color: vec4): vec4 =
        val frame1 = makeFrame(env.TIME * 4.0f, 0.01f, 0.05f, 0.4f, 0.05f)
        val frame2 = makeFrame(env.TIME * 2.0f, 0.0075f, 0.05f, 0.35f, 0.06f)

        frame1 + frame2
    }
