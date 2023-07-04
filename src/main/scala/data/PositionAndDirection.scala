package data

import play.api.libs.json._
import data.TypeAliases._

case class PositionAndDirection(point: Point, direction: Direction)

object PositionAndDirection {

  val NORTH = "N"
  val EAST = "E"
  val SOUTH = "S"
  val WEST = "W"
  val GAUCHE = "G"

  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  implicit val positionAndDirectionRead: Reads[PositionAndDirection] = Json.reads[PositionAndDirection]
  implicit val positionAndDirectionWrite: OWrites[PositionAndDirection] = Json.writes[PositionAndDirection]

  def updatePosition(previousPositionAndDirection: PositionAndDirection): PositionAndDirection = {
    val previousPosition = previousPositionAndDirection.point
    previousPositionAndDirection.direction match {
      case NORTH => previousPositionAndDirection.copy(point = previousPosition.copy(y = previousPosition.y + 1))
      case EAST => previousPositionAndDirection.copy(point = previousPosition.copy(x = previousPosition.x + 1))
      case SOUTH => previousPositionAndDirection.copy(point = previousPosition.copy(y = previousPosition.y - 1))
      case WEST => previousPositionAndDirection.copy(point = previousPosition.copy(x = previousPosition.x - 1))
    }
  }

  def updateDirection(previousPositionAndDirection: PositionAndDirection, direction: String): PositionAndDirection = {
    val newDirection = previousPositionAndDirection.direction match {
      case NORTH => if(direction == GAUCHE) WEST else EAST
      case EAST => if(direction == GAUCHE) NORTH else SOUTH
      case SOUTH => if(direction == GAUCHE) EAST else WEST
      case WEST => if(direction == GAUCHE) SOUTH else NORTH
    }
    previousPositionAndDirection.copy(direction = newDirection)
  }
}