package services

import better.files.File
import data.TypeAliases._

import scala.util.{Success, Try}
import data.{Point, Result}
import play.api.libs.json.Json
import util.MyUtil.outputErrorAndExit

import scala.io.Source

case class FileService() {

  def readLinesFromFile(file: String): List[String] = {
    Try(Source.fromFile(file).getLines().toList) match {
      case Success(readFile) => readFile
      case _ => outputErrorAndExit("Could not read file")
    }

  }

  def extractGardenSizeFromString(rawPoint: Option[String]): GardenSize = {
    val gardenSize = Try(rawPoint.fold(Array("0", "0"))(value => value.split(" ")))
      .map(pointArray => Point(pointArray(0).toInt, pointArray(1).toInt))
    gardenSize match {
      case Success(value) =>
        if (value.x <= 0 || value.y <= 0) {
          outputErrorAndExit("Garden size invalid")
        } else {
          value
        }
      case _ =>
        outputErrorAndExit("Could not read or rightfully process garden size")
    }
  }

  def buildJsonOutput(dataToWrite: Result, file: File): Unit = {
    val dataJsonFormat = Json.toJson(dataToWrite)
    file.createFileIfNotExists().overwrite(Json.prettyPrint(dataJsonFormat))
    ()
  }
}
