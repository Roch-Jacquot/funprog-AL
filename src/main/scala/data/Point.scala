package data

import play.api.libs.json._
import org.scalactic._
import Requirements._


case class Point(x: Int, y: Int){
  require(x >= 0 && y >= 0)
}

object Point {

  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  implicit val pointRead: Reads[Point] = Json.reads[Point]
  implicit val pointWrite: OWrites[Point] = Json.writes[Point]

}
