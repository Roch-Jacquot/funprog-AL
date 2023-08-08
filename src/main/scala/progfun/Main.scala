package fr.esgi.al.funprog

import com.typesafe.config.ConfigFactory
import model.{FunProgResult, MowerAfterMovement}
import services.{FileService, FormatService, MowerService, ParseAndValidateService}
import better.files._

import scala.util.{Failure, Success, Try}

object Main extends App {

  // Loaded Confligs
  val config = ConfigFactory.load()
  private val dataFile =
    config.getString("application.input-file") // "src/main/resources/test"
  private val jsonOutputFile =
    config.getString("application.output-json-file").toFile
  private val csvOutputFile =
    config.getString("application.output-csv-file").toFile
  private val yamlOutputFile =
    config.getString("application.output-yaml-file").toFile

  // Services
  private val mowerService = MowerService()
  private val fileService = FileService
  private val formatService = FormatService()
  private val parseAndValidateService = ParseAndValidateService

  // Constants
  private val GARDEN_SIZE_LINE = 1

  // Core Program

  // Reading file and building Mowers and Limits
  val data = fileService
    .readLinesFromFile(dataFile)
    .flatMap(lines => {
      for {
        gardenSize <- parseAndValidateService.extractGardenSizeFromString(lines.headOption)
        mowersAtInitialPosition <- parseAndValidateService.buildMowersFromLines(lines.drop(GARDEN_SIZE_LINE))
      } yield (gardenSize, mowersAtInitialPosition)
    })

  // Calculate final Mower positions
  val mowers = data.flatMap { gardenSizeAndMowers =>
    val mowersAtFinalPositions = Try(gardenSizeAndMowers._2.map(mower => {
      val finalPositionAndDirection = mowerService.moveMower(
        mower.debut,
        mower.instructions,
        gardenSizeAndMowers._1
      )
      MowerAfterMovement.fromMowerAtStart(mower, finalPositionAndDirection)
    }))
    mowersAtFinalPositions
  }

  val resultMowers = for {
    gardenSize            <- data
    mowersAtFinalPosition <- mowers
  } yield FunProgResult(gardenSize._1, mowersAtFinalPosition)

  // Write files
  val finalResult = resultMowers.flatMap { result =>
    fileService
      .writeJsonOutput(formatService.buildJsonOutput(result), jsonOutputFile)
      .flatMap(_ =>
        fileService
          .writeCsvOutput(formatService.buildCsvOutput(result), csvOutputFile)
          .flatMap(_ => fileService.writeYamlOutput(
            formatService.buildYamlOutput(result),
            yamlOutputFile)
      )
      )
  }

  finalResult match {
    case Success(_)       => println("Files created successfully")
    case Failure(failedValue) => println("Execution failed due to : "+ failedValue.toString)
  }

}
