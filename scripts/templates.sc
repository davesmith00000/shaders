import $ivy.`com.lihaoyi::scalatags:0.8.2`

object HomePage {

  import scalatags.Text.all._

  def page(projectList: List[String]) =
    html(
      head(title     := "Dave's Shader List")(
        meta(charset := "UTF-8")//,
        // script(src := "https://cdn.tailwindcss.com")()
      ),
      body(
        p(
          "Click on any of the links below."
        ),
        ul()(
          projectList.map { prj =>
            li(
              a(href := s"./$prj")(prj)
            )
          }
        )
      )
    )

}

object IndigoIndex {

  def page(pageName: String): String =
    s"""
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>$pageName</title>
    <style>
      body {
        background-color: black;
      }
      p {
        color: white;
      }
    </style>
  </head>
  <body>
    <div id="indigo-container"></div>
    <script type="text/javascript" src="scripts/out.js"></script>
    <script type="text/javascript">
      IndigoGame.launch("indigo-container")
    </script>
    <p>Hit 'f' for fullscreen.</p>
  </body>
</html>
    """.trim

}
