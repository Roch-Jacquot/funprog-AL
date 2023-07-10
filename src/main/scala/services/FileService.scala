package services

import better.files.File
import data.TypeAliases._

import scala.util.{Failure, Try}
import data.Point
import util.FunProgExceptions._

case class FileService() {
  private val defaultInvalidGardenSize = Array("0", "0")

  def readLinesFromFile(file: String): Try[List[String]] = {
    Try(File(file).lines.toList).recoverWith(exception =>
      Failure(FileOpeningException(exception))
    )
  }

  def extractGardenSizeFromString(rawPoint: Option[String]): Try[GardenSize] = {
    Try(rawPoint.fold(defaultInvalidGardenSize)(value => value.split(" ")))
      .map(pointArray => Point(pointArray(0).toInt, pointArray(1).toInt))
      .recoverWith(exception => Failure(GardenSizeException(exception)))
  }

  def writeJsonOutput(dataToWrite: String, file: File): Try[File] = {
    Try(file.createFileIfNotExists().overwrite(dataToWrite))
      .recoverWith(exception => Failure(FileWritingException(exception)))
  }

  def writeCsvOutput(dataCsvFormat: String, file: File): Try[File] = {
    Try(file.createFileIfNotExists().overwrite(dataCsvFormat))
      .recoverWith(exception => Failure(FileWritingException(exception)))
  }

  def writeYamlOutput(yamlDataFormat: List[String], file: File): Try[File] = {
    Try {
      val blankFile = file.createFileIfNotExists().overwrite("")
      yamlDataFormat.foreach(element => blankFile.appendLine(element))
      blankFile
    }.recoverWith(exception => Failure(FileWritingException(exception)))
  }
}
