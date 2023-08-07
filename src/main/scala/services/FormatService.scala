package services

import model.{FunProgResult, Mower}
import play.api.libs.json.Json

case class FormatService() {

  private val csvColumns = {
    "numéro;début_x;début_y;début_direction;fin_x;fin_y;fin_direction;instructions\n"
  }
  private val instructions = "instructions:"
  private val directionKey = "direction: "
  private val basicSpacing = " "
  private val xKey = "x: "
  private val yKey = "y: "
  private val yamlBorder = "---"
  private val mowerFrench = "tondeuses:"
  private val garden = "limite:"
  private val startPoint = "- debut:"
  private val endPoint = "fin:"
  private val point = "point:"

  def buildCsvOutput(dataToWrite: FunProgResult): String = {
    def getCsvLineFromData(mower: Mower, index: Int): String = {
      val (xFin, yFin, direction) = extractFinFromMower(mower)
      s"${index.toString};${mower.debut.point.x.toString};${mower.debut.point.y.toString};${mower.debut.direction.entryName};" +
        s"$xFin;$yFin;$direction;" +
        s"${mower.instructions.map(_.entryName).mkString}"
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
    val result = List(
      yamlBorder,
      garden,
      basicSpacing + xKey + dataToWrite.limite.x.toString,
      basicSpacing + yKey + dataToWrite.limite.y.toString,
      mowerFrench
    )

    val mowers =
      dataToWrite.tondeuses.flatMap(mower => mowerToYamlFormat(mower))
    result.appendedAll(mowers).appended(yamlBorder)
  }
  private def pointToYamlFormat(
      x: String,
      y: String,
      repeat: Int): List[String] =
    List(
      basicSpacing.repeat(repeat).concat(point),
      basicSpacing.repeat(repeat + 2).concat(xKey + x),
      basicSpacing.repeat(repeat + 2).concat(yKey + y)
    )
  private def mowerToYamlFormat(mower: Mower): List[String] = {
    val (xFin, yFin, direction) = extractFinFromMower(mower)

    List(startPoint) :::
      pointToYamlFormat(
        mower.debut.point.x.toString,
        mower.debut.point.y.toString,
        4
      ) :::
      List(
        basicSpacing.repeat(4).concat(directionKey + mower.debut.direction.entryName)
      ) :::
      instructionsToYaml(mower)
        .appended(basicSpacing.repeat(2).concat(endPoint)) :::
      pointToYamlFormat(xFin, yFin, 4)
        .appended(basicSpacing.repeat(4).concat(directionKey + direction))
  }

  private def extractFinFromMower(mower: Mower): (String, String, String) = {
    mower.fin
      .map(posAndDirAndInstr =>
        (
          posAndDirAndInstr.point.x.toString,
          posAndDirAndInstr.point.y.toString,
          posAndDirAndInstr.direction.entryName
        )
      )
      .getOrElse(("", "", ""))
  }

  private def instructionsToYaml(mower: Mower): List[String] = {
    List(basicSpacing.repeat(2).concat(instructions)) ::: mower.instructions
      .map(instr => "  - " + instr.entryName)
  }
}
