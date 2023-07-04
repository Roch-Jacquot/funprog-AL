package data

import data.TypeAliases.GardenSize
import play.api.libs.json.{Json, OWrites, Reads}

case class Result(
                 limite: GardenSize,
                 tondeuses: List[Mower]
                 )

object Result {
  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  implicit val resultRead: Reads[Result] = Json.reads[Result]
  implicit val resultWrite: OWrites[Result] = Json.writes[Result]
}
