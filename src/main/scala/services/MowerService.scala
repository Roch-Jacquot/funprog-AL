package services

import model.Direction._
import model.Instruction._
import model.{Instruction, PositionAndDirection}
import model.TypeAliases._

import scala.annotation.tailrec
case class MowerService() {

  private val CURRENT_INSTRUCTION = 1

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
      instructions: List[Instruction],
      gardenSize: GardenSize): PositionAndDirection = {
      instructions match {
        case instruction::_ if instruction == Forward && isNextAdvanceValid(positionAndDirection, gardenSize) =>
            val nextPositionWithDirection =
              PositionAndDirection.updatePosition(positionAndDirection)
            moveMower(
              nextPositionWithDirection,
              instructions.drop(CURRENT_INSTRUCTION),
              gardenSize
            )
        case instruction::_ if instruction == Forward =>
            moveMower(
              positionAndDirection,
              instructions.drop(CURRENT_INSTRUCTION),
              gardenSize
            )
        case instruction::_ if instruction == Left || instruction == Right =>
          val nextDirectionWithPosition = PositionAndDirection.updateDirection(
            positionAndDirection,
            instruction
          )
          moveMower(
            nextDirectionWithPosition,
            instructions.drop(CURRENT_INSTRUCTION),
            gardenSize
          )
        case _ => {
          positionAndDirection
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
      case North => positionAndDirection.point.y < gardenSize.y
      case South => positionAndDirection.point.y > 0
      case East  => positionAndDirection.point.x < gardenSize.x
      case West  => positionAndDirection.point.x > 0
    }
  }
}
