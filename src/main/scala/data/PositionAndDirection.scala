package data

import play.api.libs.json._
import org.scalactic._
import Requirements._
import Directions._
import Instructions._

case class PositionAndDirection(point: Point, direction: String) {
  require("NSEW".contains(direction))
  require(direction.size == 1)
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
    Directions.withName(previousPositionAndDirection.direction) match {
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
      turn: String): PositionAndDirection = {
    val newDirection = Directions.withName(previousPositionAndDirection.direction) match {
      case North => if (Instructions.withName(turn) == Left) "W" else "E"
      case East  => if (Instructions.withName(turn) == Left) "N" else "S"
      case South => if (Instructions.withName(turn) == Left) "E" else "W"
      case West  => if (Instructions.withName(turn) == Left) "S" else "N"
    }
    previousPositionAndDirection.copy(direction = newDirection)
  }
}
