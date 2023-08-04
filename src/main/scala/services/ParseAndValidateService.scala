package services

import data.{Mower, Point}
import data.TypeAliases.GardenSize

import scala.util.{Failure, Success, Try}
import util.FunProgExceptions._
case object ParseAndValidateService {

  def extractGardenSizeFromString(rawPoint: Option[String]): Try[GardenSize] = {
    val extractedValues = rawPoint match {
      case Some(points) => Success(points.split(" "))
      case _ => Failure(new NoSuchElementException)
    }
    extractedValues.map(pointArray => Point(pointArray(0).toInt, pointArray(1).toInt))
      .recoverWith(exception => Failure(GardenSizeException(exception)))
  }

  /**
   * * buildMowersFromLines attempts to transform the list of strings containing
   * the positions and instructions into a Try[List[Mowers]
   *
   * @param rawPositionsAndInstructions
   * @return
   */
  def buildMowersFromLines(
                            rawPositionsAndInstructions: List[String]): Try[List[Mower]] = {
    val POSITION_AND_INSTRUCTION_LINES = 2
    val POSITION_AND_DIRECTION_LINE = 0
    val INSTRUCTIONS_LINE = 1
    val X_POSITION = 0
    val Y_POSITION = 1
    val DIRECTION_POSITION = 2

    Try(
      rawPositionsAndInstructions
        .grouped(POSITION_AND_INSTRUCTION_LINES)
        .map(lines => {
          val startingPositionAndDirection =
            lines(POSITION_AND_DIRECTION_LINE).split(" ")
          val instructions = lines(INSTRUCTIONS_LINE).split("").toList
          Mower.mower(
            startingPositionAndDirection(X_POSITION).toInt,
            startingPositionAndDirection(Y_POSITION).toInt,
            startingPositionAndDirection(
              DIRECTION_POSITION
            ),
            instructions
          )
        })
        .toList
    ).recoverWith(exception => Failure(MowerMovementException(exception)))
  }

}
