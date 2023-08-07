package model

import play.api.libs.json._
import Direction._
import Instruction._

case class PositionAndDirection(point: Point, direction: Direction) {
}

object PositionAndDirection {

  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  implicit val positionAndDirectionRead: Reads[PositionAndDirection] =
    Json.reads[PositionAndDirection]
  implicit val positionAndDirectionWrite: OWrites[PositionAndDirection] =
    Json.writes[PositionAndDirection]

  /**
   * updatePosition determine the next position of a mower
   * @param previousPositionAndDirection
   * @return PositionAndDirection
   */
  def updatePosition(previousPositionAndDirection: PositionAndDirection)
      : PositionAndDirection = {
    val previousPosition = previousPositionAndDirection.point
    previousPositionAndDirection.direction match {
      case North =>
        previousPositionAndDirection.copy(point =
          previousPosition.copy(y = previousPosition.y + 1)
        )
      case East =>
        previousPositionAndDirection.copy(point =
          previousPosition.copy(x = previousPosition.x + 1)
        )
      case South =>
        previousPositionAndDirection.copy(point =
          previousPosition.copy(y = previousPosition.y - 1)
        )
      case West =>
        previousPositionAndDirection.copy(point =
          previousPosition.copy(x = previousPosition.x - 1)
        )
    }
  }

  /**
   *
   * @param previousPositionAndDirection takes a instruction that represent a change of direction and returns the
   * new direction in a PositionAndDirection object
   * @param turn
   * @return PositionAndDirection
   */
  def updateDirection(
      previousPositionAndDirection: PositionAndDirection,
      turn: Instruction): PositionAndDirection = {
    val newDirection: Direction = previousPositionAndDirection.direction match {
      case North if turn == Left => West
      case North if turn == Right => East
      case East  if turn == Left  => North
      case East  if turn == Right => South
      case South if turn == Left  => East
      case South if turn == Right => West
      case West  if turn == Left  => South
      case _ => North
    }
    previousPositionAndDirection.copy(direction = newDirection)
  }
}
