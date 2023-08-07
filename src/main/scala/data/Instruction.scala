package data

import enumeratum.{Enum, EnumEntry, PlayJsonEnum}

sealed abstract class Instruction(override val entryName: String) extends EnumEntry

object Instruction extends Enum[Instruction] with PlayJsonEnum[Instruction]{

  val values = findValues

  case object Left extends Instruction("G")

  case object Right extends Instruction("D")

  case object Forward extends Instruction("A")

}
