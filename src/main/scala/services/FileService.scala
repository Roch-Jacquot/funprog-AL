package services

import better.files.File

import scala.util.{Failure, Try}
import util.FunProgExceptions._

case object FileService {

  /**
   * readLinesFromFile reads a predetermined file
   * @param file text file found in the configuration file
   * @return Try[List[String]]
   */
  def readLinesFromFile(file: String): Try[List[String]] = {
    Try(File(file).lines.toList).recoverWith(exception =>
      Failure(FileOpeningException(exception))
    )
  }

  /**
   *  writeJsonOutput writes a file as a Json
   * @param dataToWrite
   * @param file
   * @return Try[File]
   */
  def writeJsonOutput(dataToWrite: String, file: File): Try[File] = {
    Try(file.createFileIfNotExists()
      .overwrite(dataToWrite))
      .recoverWith(exception =>
        Failure(FileWritingException(exception)))
  }

  /**
   * writeCsvOutput writes a file as a csv
   * @param dataCsvFormat
   * @param file
   * @return Try[File]
   */
  def writeCsvOutput(dataCsvFormat: String, file: File): Try[File] = {
    Try(file.createFileIfNotExists()
      .overwrite(dataCsvFormat))
      .recoverWith(exception =>
        Failure(FileWritingException(exception)))
  }

  /**
   * writeYamlOutput writes a file as a Yaml
   * @param yamlDataFormat
   * @param file
   * @return Try[File]
   */
  def writeYamlOutput(yamlDataFormat: String, file: File): Try[File] = {
    Try {
      file.createFileIfNotExists()
        .overwrite(yamlDataFormat)
    }.recoverWith(exception =>
      Failure(FileWritingException(exception)))
  }
}
