package services

import data.{Mower, PositionAndDirection}
import data.TypeAliases._
import util.MyUtil.outputErrorAndExit

import scala.util.{Failure, Success, Try}
import scala.annotation.tailrec
import util.MyException._
case class MowerService() {

  private val FIRST_LINE = 1
  private val POSITION_AND_INSTRUCTION_LINES = 2
  private val POSITION_AND_DIRECTION_LINE = 0
  private val INSTRUCTIONS_LINE = 1
  private val X_POSITION = 0
  private val Y_POSITION = 1
  private val DIRECTION_POSITION = 2
  private val CURRENT_INSTRUCTION = 1
  private val MOVE_FORWARD = "A"
  private val LEFT_RIGHT_INSTRUCTIONS = "GD"
  private val NORTH = "N"
  private val EAST = "E"
  private val SOUTH = "S"
  private val WEST = "W"

  /**
   * * buildMowersFromLines attempts to transform the list of strings
   * containing the positions and instructions into a Try[List[Mowers]
   *
   * @param rawPositionsAndInstructions
   * @return
   */
  def buildMowersFromLines(
      rawPositionsAndInstructions: List[String]): Try[List[Mower]] = {
    Try(
      rawPositionsAndInstructions
        .drop(FIRST_LINE)
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
            ), // add validity test
            instructions
          )
        })
        .toList
    )
  }
/*
  /**
   * * buildMowersFromLines checks the return of tryTobuildMowersFromLines and
   * return is value or an error
   *
   * @param rawPositionsAndInstructions
   * @return
   */
  def buildMowersFromLines(
      rawPositionsAndInstructions: List[String]): List[Mower] = {
    tryToBuildMowersFromLines(rawPositionsAndInstructions) match {
      case Success(mowerList) =>
        if (mowerList.nonEmpty) mowerList
        else outputErrorAndExit("No mowers found")
      case _ => outputErrorAndExit("Could not parse file for mowers")
    }
  }
*/
  // Function de traitement: Prend un truc (probablement objet avec valeurs de départ/tenter interface)
  // lance fonction qui prend actual position + direction, ListOfInstruction and GardenSize : return final position + Dir
  // Calls itself with new coords + new direction +new list of instructions
  // end up cipying entry object + extra info
  // Benefit !

  /**
   * moveMower moves the mower from its initial to its final position using the
   * instructions
   *
   * @param positionAndDirection
   * @param instructions
   * @param gardenSize
   * @return
   */
  @tailrec
  final def moveMower(
      positionAndDirection: PositionAndDirection,
      instructions: Instruction,
      gardenSize: GardenSize): Try[PositionAndDirection] = {
    if (instructions.isEmpty) Success(positionAndDirection)
    else {
      println(instructions.headOption)
      instructions.headOption match {
        case Some(instruction) if (instruction == MOVE_FORWARD) =>
          if (isNextAdvanceValid(positionAndDirection, gardenSize)) {
            val nextPositionWithDirection =
              PositionAndDirection.updatePosition(positionAndDirection)
            moveMower(
              nextPositionWithDirection,
              instructions.drop(CURRENT_INSTRUCTION),
              gardenSize
            )
          } else {
            moveMower(
              positionAndDirection,
              instructions.drop(CURRENT_INSTRUCTION),
              gardenSize
            )
          }
        case Some(instruction)
            if LEFT_RIGHT_INSTRUCTIONS.contains(instruction) => {
          val nextDirectionWithPosition = PositionAndDirection.updateDirection(
            positionAndDirection,
            instruction
          )
          moveMower(
            nextDirectionWithPosition,
            instructions.drop(CURRENT_INSTRUCTION),
            gardenSize
          )
        }
        case _ =>
          Failure(FileOpeningException("test"))
      }
    }
  }

  /**
   * isNextAdvanceValid checks whether the next move can actually happen by
   * making sure the current position is lower than the minimum and maximum
   * garden size
   *
   * @param positionAndDirection
   * @param gardenSize
   * @return
   */
  private def isNextAdvanceValid(
      positionAndDirection: PositionAndDirection,
      gardenSize: GardenSize): Boolean = {
    positionAndDirection.direction match {
      case NORTH => positionAndDirection.point.y < gardenSize.y
      case SOUTH => positionAndDirection.point.y > 0
      case EAST  => positionAndDirection.point.x < gardenSize.x
      case WEST  => positionAndDirection.point.x > 0
      case _     => outputErrorAndExit("Direction is not a valid parameter")
    }
  }
}
