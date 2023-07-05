package fr.esgi.al.funprog

import com.typesafe.config.ConfigFactory
//import data.ResultingWork
import services.{FileService, MowerService}
//import better.files._
//import scala.util.Try

object Main extends App {

  // Loaded Confligs
  val config = ConfigFactory.load()
  private val dataFile =
    config.getString("application.input-file") // "src/main/resources/test"
  //private val jsonOutputFile =
    //config.getString("application.output-json-file").toFile
  //private val csvOutputFile =
    //config.getString("application.output-csv-file").toFile

  // Services
  private val mowerService = MowerService()
  private val fileService = FileService()

  // Core Program

  // Reading file and building Mowers and Limits
  val data = fileService.readLinesFromFile(dataFile)
    .flatMap(lines =>{
      fileService.extractGardenSizeFromString(lines.headOption).flatMap(
        gardenSize => mowerService.buildMowersFromLines(lines).map(mowersAtInitialPosition =>
          (gardenSize, mowersAtInitialPosition)
        )
      )
    })

  println(data)
/*
  // Calculate final Mower positions
  private val mowersAtFinalPositions = mowersAtInitialPosition.map(mower => {
    val finalPositionAndDirection =
      mowerService.moveMower(mower.debut, mower.instructions, gardenSize)
    mower.copy(fin = Some(finalPositionAndDirection))
  })

  private val result = ResultingWork(gardenSize, mowersAtFinalPositions)

  // Write files
  fileService.buildJsonOutput(result, jsonOutputFile)

  fileService.buildCsvOutput(result, csvOutputFile)
*/
}
