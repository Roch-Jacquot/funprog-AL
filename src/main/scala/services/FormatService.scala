package services

import model.{FunProgResult, MowerAfterMovement}
import play.api.libs.json.Json
import util.YamlConverters.YamlOps

case class FormatService() {

  private val csvColumns = {
    "numéro;début_x;début_y;début_direction;fin_x;fin_y;fin_direction;instructions\n"
  }

  def buildCsvOutput(dataToWrite: FunProgResult): String = {
    def getCsvLineFromData(mower: MowerAfterMovement, index: Int): String = {
      val (xFin, yFin, direction) = extractFinFromMower(mower)
      s"${index.toString};" +
        s"${mower.debut.point.x.toString};" +
        s"${mower.debut.point.y.toString};" +
        s"${mower.debut.direction.entryName};" +
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

  def buildYamlOutput(dataToWrite: FunProgResult): String = {
    dataToWrite.toYaml.stringify(0, isFirstLineOfArray = false)
  }

  private def extractFinFromMower(mower: MowerAfterMovement): (String, String, String) = {
        (
          mower.fin.point.x.toString,
          mower.fin.point.y.toString,
          mower.fin.direction.entryName
        )
  }

}
