package model

import play.api.libs.json._

case class Point(x: Int, y: Int)

object Point {

  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  implicit val pointRead: Reads[Point] = Json.reads[Point]
  implicit val pointWrite: OWrites[Point] = Json.writes[Point]

}
