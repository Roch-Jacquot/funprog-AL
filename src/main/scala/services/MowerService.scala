package services

import data.PositionAndDirection
import data.TypeAliases._

import scala.annotation.tailrec
case class MowerService() {

  private val CURRENT_INSTRUCTION = 1
  private val MOVE_FORWARD = "A"
  private val LEFT_RIGHT_INSTRUCTIONS = "GD"
  private val NORTH = "N"
  private val EAST = "E"
  private val SOUTH = "S"
  private val WEST = "W"


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
      instructions: List[String],
      gardenSize: GardenSize): PositionAndDirection = {
      instructions.headOption match {
        case Some(instruction) if instruction == MOVE_FORWARD =>
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
            if LEFT_RIGHT_INSTRUCTIONS.contains(instruction) =>
          val nextDirectionWithPosition = PositionAndDirection.updateDirection(
            positionAndDirection,
            instruction
          )
          moveMower(
            nextDirectionWithPosition,
            instructions.drop(CURRENT_INSTRUCTION),
            gardenSize
          )
        case _ => positionAndDirection
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
    }
  }
}
