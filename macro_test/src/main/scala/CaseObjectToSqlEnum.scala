import scala.meta._
import java.io.PrintWriter
import org.scalameta.logger

object CaseObjectToSqlEnum extends App {
  private val inputFile = args(0)
  private val outputFile = args(1)
  private val path = java.nio.file.Paths.get(inputFile)
  private val bytes = java.nio.file.Files.readAllBytes(path)
  private val text = new String(bytes, "UTF-8")
  private val input = Input.VirtualFile(path.toString, text)
  private val fileTree = input.parse[Source].get

  private def collectEnumFromListStat(traitName: String, stat: List[Stat]) =
    stat.flatMap(_.collect {
      case de @ Defn.Object(
            s :+ Mod.Case(),
            name,
            Template(
              Nil,
              List(
                Init(
                  Type.Name(`traitName`),
                  Name(""),
                  Nil
                )
              ),
              Self(Name(""), None),
              Nil
            )
          ) =>
        logger.debug(s"${traitName}, ${name}")
        name.toString
    })

  private def caseObjectExtendsEnumTreeList(tree: Tree) =
    tree.collect {
      case defn @ Defn.Object(
            _ :+ Mod.Case(),
            _,
            Template(
              Nil,
              List(
                Init(
                  Type.Apply(Type.Name("Enum"), typename),
                  Name(""),
                  Nil
                )
              ),
              Self(Name(""), None),
              list
            )
          ) =>
        logger.debug(s"${typename.head.toString} -> ${list}")
        typename.head.toString -> collectEnumFromListStat(
          typename.head.toString,
          list
        )
    }

  private val list = caseObjectExtendsEnumTreeList(fileTree)

  private def camelToSnake(s: String): String = s.foldLeft("") { (acc, c) =>
    (c.isUpper, acc.isEmpty, acc.takeRight(1) == "_") match {
      case (true, false, false) => acc + "_" + c.toLower
      case (true, _, _)         => acc + c.toLower
      case (false, _, _)        => acc + c
    }
  }

  private def quoteListElement(list: List[String]): List[String] =
    list.map(s => "'" + s + "'")

  private val formattedList = list.map { case (st, li) =>
    (camelToSnake(st), quoteListElement(li))
  }

  private val file = new PrintWriter(outputFile)
  formattedList.headOption.foreach { case (key, list) =>
    file.print(
      s"""create type "${key}" as enum (${list.mkString(", ")});"""
    )
  }
  formattedList.drop(1).foreach { case (key, list) =>
    file.print(
      s"""\n\ncreate type "${key}" as enum (${list.mkString(", ")});"""
    )
  }

  file.close()

}
