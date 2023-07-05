package data

import util.MyUtil.outputErrorAndExit
import play.api.libs.json._
import data.TypeAliases._

case class PositionAndDirection(point: Point, direction: Direction)

object PositionAndDirection {

  private val NORTH = "N"
  private val EAST = "E"
  private val SOUTH = "S"
  private val WEST = "W"
  private val GAUCHE = "G"

  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  implicit val positionAndDirectionRead: Reads[PositionAndDirection] =
    Json.reads[PositionAndDirection]
  implicit val positionAndDirectionWrite: OWrites[PositionAndDirection] =
    Json.writes[PositionAndDirection]

  def updatePosition(previousPositionAndDirection: PositionAndDirection)
      : PositionAndDirection = {
    val previousPosition = previousPositionAndDirection.point
    previousPositionAndDirection.direction match {
      case NORTH =>
        previousPositionAndDirection.copy(point =
          previousPosition.copy(y = previousPosition.y + 1)
        )
      case EAST =>
        previousPositionAndDirection.copy(point =
          previousPosition.copy(x = previousPosition.x + 1)
        )
      case SOUTH =>
        previousPositionAndDirection.copy(point =
          previousPosition.copy(y = previousPosition.y - 1)
        )
      case WEST =>
        previousPositionAndDirection.copy(point =
          previousPosition.copy(x = previousPosition.x - 1)
        )
      case _ =>
        outputErrorAndExit("Direction not valid")
    }
  }

  def updateDirection(
      previousPositionAndDirection: PositionAndDirection,
      direction: String): PositionAndDirection = {
    val newDirection = previousPositionAndDirection.direction match {
      case NORTH => if (direction == GAUCHE) WEST else EAST
      case EAST  => if (direction == GAUCHE) NORTH else SOUTH
      case SOUTH => if (direction == GAUCHE) EAST else WEST
      case WEST  => if (direction == GAUCHE) SOUTH else NORTH
      case _     => outputErrorAndExit("Direction not valid")
    }
    previousPositionAndDirection.copy(direction = newDirection)
  }
}
