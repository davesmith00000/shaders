
import $ivy.`com.lihaoyi::scalatags:0.8.2`

object HomePage {

  import scalatags.Text.all._

  def page(projectList: List[String]) =
    html(
      head(title := "Dave's Shader List")(
        meta(charset := "UTF-8"),
      ),
      body(
        projectList.map { prj =>
          p(prj)
        }
      )
    )

}

// Not cross compiled for Scala 2.13...
// import $ivy.`io.indigoengine::tyrian:0.6.1`

// object HomePage {

//   import tyrian.*
//   import tyrian.Html.*

//   def page(projectList: List[String]): Html[Nothing] =
//     html(
//       head(
//         meta(charset := "UTF-8"),
//         title("Dave's Shader List")
//       ),
//       body(
//         projectList.map { prj =>
//           p(prj)
//         }
//       )
//     )

// }
