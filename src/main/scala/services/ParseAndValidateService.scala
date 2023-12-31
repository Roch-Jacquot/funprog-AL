package services

import model.{Direction, Instruction, MowerAtStart, Point}
import model.TypeAliases.GardenSize

import scala.util.{Failure, Success, Try}
import util.FunProgExceptions._
case object ParseAndValidateService {

  /**
   * extractGardenSizeFromString returns the formated point that represents the limits of the garden
   * GardenSize is a type alias of point
   * @param rawPoint
   * @return Try[GardenSize]
   */
  def extractGardenSizeFromString(rawPoint: Option[String]): Try[GardenSize] = {
    val extractedValues = rawPoint match {
      case Some(points) if points.contains('-') => Failure(new IllegalArgumentException())
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
   * @return Try[List[MowerAtStart]]
   */

  def buildMowersFromLines(
                            rawPositionsAndInstructions: List[String]): Try[List[MowerAtStart]] = {
    val POSITION_AND_INSTRUCTION_LINES = 2
    val POSITION_AND_DIRECTION_LINE = 0
    val INSTRUCTIONS_LINE = 1
    val X_POSITION = 0
    val Y_POSITION = 1
    val DIRECTION_POSITION = 2
    val EMPTY_SEPARATOR = ""
    val SPACE_SEPARATOR = " "
    val MIN_POSITION = 0

      Try(
        rawPositionsAndInstructions
          .grouped(POSITION_AND_INSTRUCTION_LINES)
          .map { lines =>
            val startingPositionAndDirection =
              lines(POSITION_AND_DIRECTION_LINE).split(SPACE_SEPARATOR)
            val instructions = lines(INSTRUCTIONS_LINE).split(EMPTY_SEPARATOR)
              .map(instruction => Instruction.withName(instruction))
              .toList

            MowerAtStart.mower(
              startingPositionAndDirection(X_POSITION).toInt,
              startingPositionAndDirection(Y_POSITION).toInt,
              Direction.withName(startingPositionAndDirection(
                DIRECTION_POSITION
              )),
              instructions
            )
          }.toList)
        .flatMap(mowers => {
          val debutPoints = mowers.map(mower => mower.debut)
          val negativeValues = debutPoints.flatMap(positionAndDirection =>
            List(positionAndDirection.point.x, positionAndDirection.point.y)).filter(_ < MIN_POSITION)
          negativeValues match {
            case _::_ => Failure(new IllegalArgumentException)
            case _ => Success(mowers)
          }
        })
        .recoverWith(exception => Failure(MowerParsingException(exception)))
  }

}
