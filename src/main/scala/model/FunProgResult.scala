package model

import model.TypeAliases.GardenSize
import play.api.libs.json.{Json, OWrites, Reads}

case class FunProgResult(
    limite: GardenSize,
    tondeuses: List[MowerAfterMovement]
)

object FunProgResult {
  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  implicit val funProgResultRead: Reads[FunProgResult] =
    Json.reads[FunProgResult]
  implicit val funProgResultWrite: OWrites[FunProgResult] =
    Json.writes[FunProgResult]
}
