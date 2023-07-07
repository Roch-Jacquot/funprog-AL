package services

import data.{FunProgResult, Mower}
import play.api.libs.json.Json

case class FormatService() {

  private val csvColumns =
    "numéro;début_x;début_y;début_direction;fin_x;fin_y;fin_direction;instructions\n"

  def buildCsvOutput(dataToWrite: FunProgResult): String = {
    def getCsvLineFromData(data: Mower, index: Int): String = {
      val (xFin, yFin, direction) = data.fin
        .map(posAndDirAndInstr =>
          (posAndDirAndInstr.point.x.toString, posAndDirAndInstr.point.y.toString, posAndDirAndInstr.direction)).getOrElse(("", "", ""))
      s"${index.toString};${data.debut.point.x.toString};${data.debut.point.y.toString};${data.debut.direction};" +
        s"$xFin;$yFin;$direction;" +
        s"${data.instructions.mkString}"
    }

    dataToWrite.tondeuses.zipWithIndex
      .map(mower => getCsvLineFromData(mower._1, mower._2))
      .mkString("\n")
      .prependedAll(csvColumns)
  }

  def buildJsonOutput(dataToWrite: FunProgResult): String = {
    Json.prettyPrint(Json.toJson(dataToWrite))
  }

  def buildYamlOutput(dataToWrite: FunProgResult): List[String] = {
    val result = List("---",
      "limite:", " x: " + dataToWrite.limite.x.toString
      , " y: " + dataToWrite.limite.y.toString
      , "tondeuses:")

    val mowers = dataToWrite.tondeuses.flatMap(mower => mowerToYamlFormat(mower))
    result.appendedAll(mowers).appended("---")
  }
  private def pointToYamlFormat(x: String, y: String, repeat: Int): List[String] =
    List(" ".repeat(repeat).concat("point:"),
      " ".repeat(repeat + 2).concat("x: " + x + ""),
      " ".repeat(repeat + 2).concat("y: " + y + ""))
  def mowerToYamlFormat(mower: Mower): List[String] = {
    val (xFin, yFin, direction) = mower.fin
      .map(posAndDirAndInstr =>
        (posAndDirAndInstr.point.x.toString, posAndDirAndInstr.point.y.toString, posAndDirAndInstr.direction)).getOrElse(("", "", ""))

    List("- debut:").appendedAll(
      pointToYamlFormat(mower.debut.point.x.toString, mower.debut.point.y.toString, 4))
      .appendedAll(
        List(" ".repeat(4).concat("direction: " + mower.debut.direction),
          " ".repeat(2).concat("instructions:")))
      .appendedAll(mower.instructions.map(instr => "  - " + instr))
      .appended(" ".repeat(2).concat("fin:"))
      .appendedAll(pointToYamlFormat(xFin, yFin, 4))
      .appended(" ".repeat(4).concat("direction: " + direction))
  }
}
