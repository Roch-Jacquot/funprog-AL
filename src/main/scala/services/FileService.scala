package services

import better.files.File

import scala.util.{Failure, Try}
import util.FunProgExceptions._

case object FileService {

  def readLinesFromFile(file: String): Try[List[String]] = {
    Try(File(file).lines.toList).recoverWith(exception =>
      Failure(FileOpeningException(exception))
    )
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
