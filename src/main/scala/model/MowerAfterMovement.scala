package model

import play.api.libs.json._

case class MowerAfterMovement(
    debut: PositionAndDirection,
    instructions: List[Instruction],
    fin: PositionAndDirection)
object MowerAfterMovement {

  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  implicit val mowerRead: Reads[MowerAfterMovement] = Json.reads[MowerAfterMovement]
  implicit val mowerWrite: OWrites[MowerAfterMovement] = Json.writes[MowerAfterMovement]

  def mower(
      x: Int,
      y: Int,
      direction: Direction,
      instructions: List[Instruction],
      finalPosition: PositionAndDirection): MowerAfterMovement = {
    val debut = PositionAndDirection(Point(x, y), direction)
    MowerAfterMovement(debut, instructions, finalPosition)
  }

  def fromMowerAtStart(
             mowerAtStart: MowerAtStart,
             finalPosition: PositionAndDirection): MowerAfterMovement = {
    new MowerAfterMovement(mowerAtStart.debut, mowerAtStart.instructions, finalPosition)
  }
}
