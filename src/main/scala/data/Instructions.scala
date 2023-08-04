package data

import enumeratum.{Enum, EnumEntry}

sealed abstract class Instructions (override val entryName: String) extends EnumEntry

object Instructions extends Enum[Instructions]{

  val values = findValues

  case object Left extends Instructions("G")

  case object Right extends Instructions("D")

  case object Forward extends Instructions("A")

}
