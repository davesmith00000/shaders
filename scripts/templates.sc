import $ivy.`com.lihaoyi::scalatags:0.8.2`

import scalatags.Text.all._

object HomePage {

  def page(projectList: List[String]) =
    "<!DOCTYPE html>" +
      html(
        head(title     := "Dave's Shader List")(
          meta(charset := "UTF-8"),
          link(
            rel  := "stylesheet",
            href := "https://cdn.jsdelivr.net/npm/purecss@3.0.0/build/pure-min.css"
          )
        ),
        body(
          h1("Dave's Shader Reference"),
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

  def page(pageName: String) =
    "<!DOCTYPE html>" +
      html(
        head(title     := pageName)(
          meta(charset := "UTF-8"),
          link(
            rel  := "stylesheet",
            href := "https://cdn.jsdelivr.net/npm/purecss@3.0.0/build/pure-min.css"
          )
        ),
        body(
          div(id := "indigo-container")(),
          script(tpe := "text/javascript", src := "scripts/out.js")(),
          script(tpe := "text/javascript")(
            """IndigoGame.launch('indigo-container')"""
          ),
          p("Hit 'f' for fullscreen.")
        )
      )

}
