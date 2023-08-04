package data

import enumeratum._

sealed abstract class Directions(override val entryName: String) extends EnumEntry

object Directions extends Enum[Directions]{

  val values = findValues

  case object North extends Directions("N")

  case object South extends Directions("S")

  case object East extends Directions("E")

  case object West extends Directions("W")
}
