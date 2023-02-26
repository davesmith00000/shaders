import $ivy.`com.lihaoyi::scalatags:0.8.2`

object HomePage {

  import scalatags.Text.all._

  def page(projectList: List[String]) =
    html(
      head(title     := "Dave's Shader List")(
        meta(charset := "UTF-8")
      ),
      body(
        p(
          "These open in a new target for now because the back button won't work... I'll fix it soon!"
        ),
        projectList.map { prj =>
          p(
            a(href := s"./$prj", target := "_blank")(prj)
          )
        }
      )
    )

}
