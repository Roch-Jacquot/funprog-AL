package model

import enumeratum._

sealed abstract class Direction(override val entryName: String)
    extends EnumEntry

object Direction extends Enum[Direction] with PlayJsonEnum[Direction] {

  val values = findValues

  case object North extends Direction("N")

  case object South extends Direction("S")

  case object East extends Direction("E")

  case object West extends Direction("W")

}
