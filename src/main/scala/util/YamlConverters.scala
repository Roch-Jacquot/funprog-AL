package util

import model.{FunProgResult, MowerAfterMovement, Point, PositionAndDirection}
import util.YamlSerialization.YamlValue
import YamlSerialization._

object YamlConverters {
  trait YamlConverter[T] {
    /**
     * convert takes values and convert them to their YamlValue
     * @param value
     * @return YamlValue of a particular type
     */
    def convert(value: T): YamlValue
  }

  implicit object PointConverter extends YamlConverter[Point] {
    override def convert(point: Point): YamlValue =
      YamlObject(Map(
        "x" -> YamlNumber(point.x),
        "y" -> YamlNumber(point.y)
      ))
  }

  implicit object PositionAndDirectionConverter extends YamlConverter[PositionAndDirection] {
    override def convert(positionAndDirection: PositionAndDirection): YamlValue =
      YamlObject(Map(
        "point" -> positionAndDirection.point.toYaml,
        "direction" -> YamlString(positionAndDirection.direction.entryName)
      ))
  }

  implicit object MowerAfterMovementConverter extends YamlConverter[MowerAfterMovement] {
    override def convert(mowerAfterMovement: MowerAfterMovement): YamlValue =
      YamlObject(Map(
        "debut" -> mowerAfterMovement.debut.toYaml,
        "instructions" -> YamlArray(mowerAfterMovement.instructions.map(
          instruction => YamlString(instruction.entryName))),
        "fin" -> mowerAfterMovement.fin.toYaml
      ))
  }

  implicit object FunProgResultConverter extends YamlConverter[FunProgResult] {
    override def convert(funProgResult: FunProgResult): YamlValue =
      YamlObject(Map(
        "limite" -> funProgResult.limite.toYaml,
        "tondeuses" -> YamlArray(funProgResult.tondeuses.map(
          tondeuse => tondeuse.toYaml))
      ))
  }

  implicit class YamlOps[T](value: T) {
    /**
     * toYaml takes a value and a converter and applies the convert function on it
     * @param converter
     * @return
     */
    def toYaml(implicit converter: YamlConverter[T]): YamlValue =
      converter.convert(value)
  }

}
