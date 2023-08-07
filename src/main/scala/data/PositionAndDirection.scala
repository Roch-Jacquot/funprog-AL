package data

import play.api.libs.json._
import Direction._
import Instruction._

case class PositionAndDirection(point: Point, direction: Direction) {
  //require("NSEW".contains(direction))
  //require(direction.size == 1)
}

object PositionAndDirection {

  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  implicit val positionAndDirectionRead: Reads[PositionAndDirection] =
    Json.reads[PositionAndDirection]
  implicit val positionAndDirectionWrite: OWrites[PositionAndDirection] =
    Json.writes[PositionAndDirection]

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

  def updateDirection(
      previousPositionAndDirection: PositionAndDirection,
      turn: Instruction): PositionAndDirection = {
    val newDirection: Direction = previousPositionAndDirection.direction match {
      case North => if (turn == Left) West else East
      case East  => if (turn == Left) North else South
      case South => if (turn == Left) East else West
      case West  => if (turn == Left) South else North
    }
    previousPositionAndDirection.copy(direction = newDirection)
  }
}
