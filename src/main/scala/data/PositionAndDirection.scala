package data

import util.MyUtil.outputErrorAndExit
import play.api.libs.json._
import data.TypeAliases._
import org.scalactic._
import Requirements._
import Directions._

case class PositionAndDirection(
                                 point: Point, direction: Direction){
  require("NSEW".contains(direction))
  require(direction.size == 1)
}

object PositionAndDirection {

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
                       turn: String): PositionAndDirection = {
    val newDirection = previousPositionAndDirection.direction match {
      case NORTH => if (turn == GAUCHE) WEST else EAST
      case EAST  => if (turn == GAUCHE) NORTH else SOUTH
      case SOUTH => if (turn == GAUCHE) EAST else WEST
      case WEST  => if (turn == GAUCHE) SOUTH else NORTH
    }
    previousPositionAndDirection.copy(direction = newDirection)
  }
}
