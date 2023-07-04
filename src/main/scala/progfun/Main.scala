package fr.esgi.al.funprog

import com.typesafe.config.ConfigFactory
import data.Result
import services.{FileService, MowerService}
import better.files._


object Main extends App {

  private val mowerService = MowerService()
  private val fileService = FileService()

  val config = ConfigFactory.load()
  private val dataFile = config.getString("application.input-file") //"src/main/resources/test"

  val data = fileService.readLinesFromFile(dataFile)
  val gardenSize = fileService.extractGardenSizeFromString(data.headOption)
  private val mowersAtInitialPosition = mowerService.buildMowersFromLines(data)

  private val mowersAtFinalPositions = mowersAtInitialPosition.map(
    mower => {
      val finalPositionAndDirection = mowerService.moveMower(mower.debut, mower.instructions, gardenSize)
      mower.copy(fin = Some(finalPositionAndDirection))
    })

  val result = Result(gardenSize, mowersAtFinalPositions)

  private val jsonOutputFile = config.getString("application.output-json-file").toFile

  fileService.buildJsonOutput(result, jsonOutputFile)

}
