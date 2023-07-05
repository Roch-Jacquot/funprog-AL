package data

import data.TypeAliases.GardenSize
import play.api.libs.json.{Json, OWrites, Reads}

case class ResultingWork(
    limite: GardenSize,
    tondeuses: List[Mower]
)

object ResultingWork {
  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  implicit val resultingWorkRead: Reads[ResultingWork] =
    Json.reads[ResultingWork]
  implicit val resultingWorkWrite: OWrites[ResultingWork] =
    Json.writes[ResultingWork]
}
