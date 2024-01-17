import indigo.*

import scala.scalajs.js.annotation.*
import generated.*

@JSExportTopLevel("IndigoGame")
object GlowingStar extends IndigoShader:

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

      // Calculate the distance of the point p from the radius r.
      def sdCircle(p: vec2, r: Float): Float =
        length(p) - r

      /** Calculate the distance of the point p from the star with radius r, and
        * a relative value rf controlling the depth of the stars points.
        */
      def sdStar5(p: vec2, r: Float, rf: Float): Float =
        @const val k1: vec2 = vec2(0.809016994375f, -0.587785252292f)
        @const val k2: vec2 = vec2(-k1.x, k1.y)

        var p2 = vec2(abs(p.x), p.y)

        p2 = p2 - 2.0f * max(dot(k1, p2), 0.0f) * k1
        p2 = p2 - 2.0f * max(dot(k2, p2), 0.0f) * k2
        p2 = vec2(abs(p2.x), p2.y - r)

        val ba: vec2 = rf * vec2(-k1.y, k1.x) - vec2(0.0f, 1.0f)
        val h: Float = clamp(dot(p2, ba) / dot(ba, ba), 0.0f, r)

        length(p2 - ba * h) * sign(p2.y * ba.x - p2.x * ba.y)

      def drawSdfCircle(sdf: Float): vec4 =
        val col = step(0.0f, -sdf)

        vec4(vec3(col), 1.0f)

      def drawSdfCircle2(sdf: Float): vec4 =
        val col = cos(abs(sdf) * 100.0f)

        vec4(vec3(col), 1.0f)

      def drawStarStep1(sdf: Float): vec4 =
        val col = step(0.0f, -sdf)

        vec4(vec3(col), 1.0f)

      def drawStarStep2(sdf: Float): vec4 =
        val col = 1.0f - step(0.025f, abs(sdf))

        vec4(vec3(col), 1.0f)

      def drawStarStep3(sdf: Float): vec4 =
        val col = smoothstep(0.8f, 1.0f, 1.0f - sdf) * 0.5f

        vec4(vec3(col), 1.0f)

      def drawStarStep4(sdf: Float): vec4 =
        val glowAmount     = smoothstep(0.9f, 1.0f, 1.0f - sdf) * 0.3f
        val starFillAmount = step(0.0f, -sdf)
        val strokeAmount   = 1.0f - step(0.025f, abs(sdf))

        val orange      = vec4(1.0f, 0.75f, 0.0f, 1.0f)
        val yellow      = vec4(1.0f, 1.0f, 0.0f, 1.0f)
        val lightYellow = vec4(1.0f, 1.0f, 0.75f, 1.0f)

        val glowColour = vec4(orange.rgb * glowAmount, glowAmount)
        val starColour = vec4(
          mix(
            yellow.rgb,
            orange.rgb,
            smoothstep(0.0f, 0.1f, abs(sdf))
          ) * starFillAmount,
          starFillAmount
        )
        val strokeColour = vec4(lightYellow.rgb * strokeAmount, strokeAmount)

        mix(
          glowColour,
          mix(
            starColour,
            strokeColour,
            strokeAmount
          ),
          starFillAmount
        )

      def fragment(color: vec4): vec4 =
        // val circleSDF = sdCircle(env.UV - 0.5f, 0.25f)

        // drawSdfCircle(circleSDF)  // Simple SDF
        // drawSdfCircle2(circleSDF) // Using it as a cos wave

        val starSDF = sdStar5((env.UV - 0.5f) * vec2(1.0f, -1.0f), 0.25f, 0.6f)

        // drawStarStep1(starSDF) // SDF
        // drawStarStep2(starSDF) // Annular
        // drawStarStep3(starSDF) // Glow
        drawStarStep4(starSDF) // Putting it all together in color
    }
