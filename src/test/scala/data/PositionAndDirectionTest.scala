package data

import org.scalatest.funsuite.AnyFunSuite

class PositionAndDirectionTest extends AnyFunSuite {

  val positionAndDirectionNorth: PositionAndDirection =
    PositionAndDirection(Point(1, 1), "N")
  val positionAndDirectionSouth: PositionAndDirection =
    PositionAndDirection(Point(1, 1), "S")
  val positionAndDirectionEast: PositionAndDirection =
    PositionAndDirection(Point(1, 1), "E")
  val positionAndDirectionWest: PositionAndDirection =
    PositionAndDirection(Point(1, 1), "W")

  test(
    "updatePosition should return a new position North of the previous one"
  ) {
    val result =
      PositionAndDirection.updatePosition(positionAndDirectionNorth).point

    assert(result === Point(1, 2))

  }
  test(
    "updatePosition should return a new position South of the previous one"
  ) {
    val result =
      PositionAndDirection.updatePosition(positionAndDirectionSouth).point

    assert(result === Point(1, 0))

  }
  test(
    "updatePosition should return a new position East of the previous one"
  ) {
    val result =
      PositionAndDirection.updatePosition(positionAndDirectionEast).point

    assert(result === Point(2, 1))

  }
  test(
    "updatePosition should return a new position West of the previous one"
  ) {
    val result =
      PositionAndDirection.updatePosition(positionAndDirectionWest).point

    assert(result === Point(0, 1))

  }

  test(
    "updateDirection Should update the direction toward East (from North)"
  ) {
    val result =
      PositionAndDirection.updateDirection(positionAndDirectionNorth, "D")

    assert(result === positionAndDirectionEast)
  }
  test(
    "updateDirection Should update the direction toward East (from South)"
  ) {
    val result =
      PositionAndDirection.updateDirection(positionAndDirectionSouth, "G")

    assert(result === positionAndDirectionEast)
  }
  test(
    "updateDirection Should update the direction toward North (from east)"
  ) {
    val result =
      PositionAndDirection.updateDirection(positionAndDirectionEast, "G")

    assert(result === positionAndDirectionNorth)
  }
  test(
    "updateDirection Should update the direction toward North (from West)"
  ) {
    val result =
      PositionAndDirection.updateDirection(positionAndDirectionWest, "D")

    assert(result === positionAndDirectionNorth)
  }
}
