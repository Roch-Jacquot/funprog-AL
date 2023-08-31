package model

import model.Direction.{East, North}

object YamlSerialization2 extends App{

  sealed trait YamlValue extends Product with Serializable{
    def stringify(spacing: Int, isFirstLineOfArray: Boolean): String
  }

  final case class YamlString(value: String) extends YamlValue{
    override def stringify(spacing: Int, isFirstLineOfArray: Boolean): String = value
  }

  final case class YamlNumber(value: Int) extends YamlValue {
    override def stringify(spacing: Int, isFirstLineOfArray: Boolean): String = value.toString
  }
  
  final case class YamlArray(values: List[YamlValue]) extends YamlValue {
    override def stringify(spacing: Int, isFirstLineOfArray: Boolean): String =
      values.map {
        case yamlObject: YamlObject =>
          yamlObject.values.toList.zipWithIndex.map {
            case ((key, value), index) if index == 0 =>
              YamlObject(Map(key -> value)).stringify(spacing, isFirstLineOfArray = true)
            case yamlValueWithIndex =>
              YamlObject(Map(yamlValueWithIndex._1._1 -> yamlValueWithIndex._1._2))
                .stringify(spacing, isFirstLineOfArray = false)
          }.mkString("\n")
        case yamlValue =>
          indenter(spacing - 2) + "- " + yamlValue.stringify(spacing, isFirstLineOfArray = false)
      }.mkString("\n")
  }

  final case class YamlObject(values: Map[String, YamlValue]) extends YamlValue {
    override def stringify(spacing: Int, isFirstLineOfArray: Boolean): String = values.map {
      case (key, value: YamlObject) if(isFirstLineOfArray) =>
        indenter(spacing-2) + "- " + key + ":\n" + value.stringify(spacing + 2, isFirstLineOfArray = false)
      case (key, value: YamlObject) =>
        indenter(spacing) + key + ":\n" + value.stringify(spacing + 2, isFirstLineOfArray = false)
      case (key, value: YamlArray) =>
        indenter(spacing) +  key + ":\n" + value.stringify(spacing + 2, isFirstLineOfArray = false)
      case (key, value) =>
        indenter(spacing) + key + ": " + value.stringify(spacing, isFirstLineOfArray = false)
    }
      .mkString("\n")
  }

  trait YamlConverter[T] {
    def convert(value: T): YamlValue
  }

  implicit object PointConverter extends YamlConverter[Point]{
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
    def toYaml(implicit converter: YamlConverter[T]): YamlValue =
      converter.convert(value)
  }

  private def indenter(spacing: Int): String =
    " " * spacing

  val limite = Point(5, 5)
  val posiSM1 = PositionAndDirection(Point(1, 2), North)
  val posiSM2 = PositionAndDirection(Point(3, 3), East)
  val posiEM1 = PositionAndDirection(Point(1, 3), North)
  val posiEM2 = PositionAndDirection(Point(5, 1), East)

  val instructionsM1 = List(Instruction.Left, Instruction.Forward, Instruction.Left, Instruction.Forward,
    Instruction.Left, Instruction.Forward, Instruction.Left, Instruction.Forward, Instruction.Forward)

  val instructionsM2 = List(Instruction.Forward, Instruction.Forward, Instruction.Right,
    Instruction.Forward, Instruction.Forward, Instruction.Right, Instruction.Forward, Instruction.Right,
    Instruction.Right, Instruction.Forward)

  val mower1: MowerAfterMovement = MowerAfterMovement(posiSM1, instructionsM1, posiEM1)
  val mower2: MowerAfterMovement = MowerAfterMovement(posiSM2, instructionsM2, posiEM2)

  println(FunProgResult(limite, List(mower1, mower2)).toYaml.stringify(0, isFirstLineOfArray = false))

}
