package input

import enumeratum.Enum
import enumeratum.EnumEntry

case object Dummy1

sealed trait Color extends EnumEntry
case object Color extends Enum[Color] {
  case object Red extends Color
  case object Green extends Color
  case object Yellow extends Color
  lazy val values: IndexedSeq[Color] = findValues
}

sealed trait DominantHand extends EnumEntry
private case object DominantHand extends Enum[DominantHand] {
  case object Right extends DominantHand
  case object Left extends DominantHand
  case object Dummy2 extends EnumEntry
  case object Dummy3
  lazy val values: IndexedSeq[DominantHand] = findValues
}

sealed trait Medal extends EnumEntry
case object Medal extends Enum[Medal] {
  case object Gold extends Medal
  case object Silver extends Medal
  case object Bronze extends Medal
  lazy val values: IndexedSeq[Medal] = findValues
}
