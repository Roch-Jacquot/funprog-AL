package data

import play.api.libs.json._
import TypeAliases._

case class Mower(
    debut: PositionAndDirection,
    instructions: List[String],
    fin: Option[PositionAndDirection]
)
object Mower {

  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  implicit val mowerRead: Reads[Mower] = Json.reads[Mower]
  implicit val mowerWrite: OWrites[Mower] = Json.writes[Mower]

  def mower(
      x: Int,
      y: Int,
      direction: Direction,
      instructions: List[String]): Mower = {
    val debut = PositionAndDirection(Point(x, y), direction)
    Mower(debut, instructions, None)
  }
}
