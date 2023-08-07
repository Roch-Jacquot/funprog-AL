package model

import play.api.libs.json._

case class MowerAtStart(
    debut: PositionAndDirection,
    instructions: List[Instruction])
object MowerAtStart {

  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  implicit val mowerRead: Reads[MowerAtStart] = Json.reads[MowerAtStart]
  implicit val mowerWrite: OWrites[MowerAtStart] = Json.writes[MowerAtStart]

  def mower(
      x: Int,
      y: Int,
      direction: Direction,
      instructions: List[Instruction]): MowerAtStart = {
    val debut = PositionAndDirection(Point(x, y), direction)
    MowerAtStart(debut, instructions)
  }
}
