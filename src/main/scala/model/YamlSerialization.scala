package model

import model.Direction.North

object YamlSerialization extends App{

  sealed trait YamlValue extends Product with Serializable{
    def stringify(spacing: Int): String
  }

  final case class YamlString(value: String) extends YamlValue{
    override def stringify(spacing: Int): String = value
  }

  final case class YamlNumber(value: Int) extends YamlValue {
    override def stringify(spacing: Int): String = value.toString
  }
  
  final case class YamlArray(values: List[YamlValue]) extends YamlValue {
    //override def stringify(spacing: Int): String = values.map(element => "\n" + " " * (spacing-2) + "- " + element.stringify(spacing)).mkString
    override def stringify(spacing: Int): String = values.map(element =>"\n" + " " * (spacing-2) + "- " + element.stringify(spacing)).mkString
      .replaceAll("-   ", "- ")

  }

  final case class YamlObject(values: Map[String, YamlValue]) extends YamlValue {
    override def stringify(spacing: Int): String = values.map{
      case (key, value:YamlObject) => " " * spacing + key +": \n" + value.stringify(spacing+2)
      case (key, value:YamlArray) => " " * spacing + key + ":" + value.stringify(spacing+2)
      case (key, value) => " " * spacing + key +": " + value.stringify(spacing)
    }
      .mkString("\n")
  }


  trait YamlConverter[T] {
    def convert(value: T): YamlValue
  }

  implicit object PointConverter extends YamlConverter[Point]{
    override def convert(point: Point): YamlValue = YamlObject(Map(
      "x" -> YamlNumber(point.x),
      "y" -> YamlNumber(point.y)
    ))
  }
  implicit object PositionAndDirectionConverter extends YamlConverter[PositionAndDirection] {
    override def convert(positionAndDirection: PositionAndDirection): YamlValue = YamlObject(Map(
      "point" -> positionAndDirection.point.toYaml,
      "direction" -> YamlString(positionAndDirection.direction.entryName)
    ))
  }

  implicit object MowerAfterMovementConverter extends YamlConverter[MowerAfterMovement] {
    override def convert(mowerAfterMovement: MowerAfterMovement): YamlValue = YamlObject(Map(
      "debut" -> mowerAfterMovement.debut.toYaml,
      "instructions" -> YamlArray(mowerAfterMovement.instructions.map(
        instruction => YamlString(instruction.entryName))),
      "fin" -> mowerAfterMovement.fin.toYaml
    ))
  }

  implicit object FunProgResultConverter extends YamlConverter[FunProgResult] {
    override def convert(funProgResult: FunProgResult): YamlValue = YamlObject(Map(
      "limite" -> funProgResult.limite.toYaml,
      "tondeuses" -> YamlArray(funProgResult.tondeuses.map(
        tondeuse => tondeuse.toYaml))
    ))
  }


  implicit class YamlOps[T](value: T) {
    def toYaml(implicit converter: YamlConverter[T]): YamlValue =
      converter.convert(value)
  }

  val point = Point(2, 3)
  val posi = PositionAndDirection(point, North)

  val mower: MowerAfterMovement = MowerAfterMovement(posi, List(Instruction.Left, Instruction.Right), posi)

  println(FunProgResult(point, List(mower, mower)).toYaml.stringify(0))

  val test = FunProgResult(point, List(mower, mower)).toYaml.toString
  println(test)
}
