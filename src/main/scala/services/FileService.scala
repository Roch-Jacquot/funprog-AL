package services

import better.files.File
import data.TypeAliases._

import scala.util.{Success, Try, Failure}
import data.{Mower, Point, PositionAndDirection, ResultingWork}
import play.api.libs.json.Json
import util.MyUtil.outputErrorAndExit
import util.MyException._

case class FileService() {
  private val xmlColumns =
    "numéro;début_x;début_y;début_direction;fin_x;fin_y;fin_direction;instructions\n"
  private val defaultInvalidPoint = Point(-1, -1)
  private val defaultInvalidDirection = "X"
  private val defaultInvalidGardenSize = Array("0", "0")

  def readLinesFromFile(file: String): Try[List[String]] = {
    Try(File(file).lines.toList).orElse(Failure(FileOpeningException("")))

  }

  def extractGardenSizeFromString(rawPoint: Option[String]): Try[GardenSize] = {
    val gardenSize =
      Try(rawPoint.fold(defaultInvalidGardenSize)(value => value.split(" ")))
        .map(pointArray => Point(pointArray(0).toInt, pointArray(1).toInt))
    gardenSize match {
      case Success(value) =>
        if (value.x <= 0 || value.y <= 0) {
          Failure(GardenSizeException("Garden size invalid or absent"))
        } else {
          Success(value)
        }
      case _ =>
        Failure(GardenSizeException("Could not parse garden size"))
    }
  }

  def buildJsonOutput(dataToWrite: ResultingWork, file: File): Unit = {
    val dataJsonFormat = Json.toJson(dataToWrite)
    Try(file.createFileIfNotExists().overwrite(Json.prettyPrint(dataJsonFormat))) match {
      case Success(_) => ()
      case _ => outputErrorAndExit("Could not write json to file")
    }
  }

  def buildCsvOutput(dataToWrite: ResultingWork, file: File): Unit = {
    def getCsvLineFromData(data: Mower, index: Int): String = {
      val fin = data.fin.getOrElse(
        PositionAndDirection(defaultInvalidPoint, defaultInvalidDirection)
      )
      s"${index.toString};${data.debut.point.x.toString};${data.debut.point.y.toString};${data.debut.direction};" +
        s"${fin.point.x.toString};${fin.point.y.toString};${fin.direction};" +
        s"${data.instructions.mkString}"
    }

    val dataCsvFormat = dataToWrite.tondeuses.zipWithIndex
      .map(mower => getCsvLineFromData(mower._1, mower._2))
      .mkString("\n")
      .prependedAll(xmlColumns)
    Try(file.createFileIfNotExists().overwrite(dataCsvFormat)) match {
      case Success(_) => ()
      case _ => outputErrorAndExit("Could not write csv to file")
    }
  }
}
