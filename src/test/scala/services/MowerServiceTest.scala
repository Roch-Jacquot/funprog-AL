package services

import data.{Mower, Point, PositionAndDirection}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class MowerServiceTest extends AnyFunSuite with Matchers {

  val mowerService = MowerService()

  test(
    """ "Hello"(6) should throw a "java.lang.StringIndexOutOfBoundsException" """
  ) {
    assertThrows[java.lang.StringIndexOutOfBoundsException]("Hello" (6))
  }

  test("buildMowersFromLines should return a valid list of Mowers") {
    val validStringList = List("1 1", "1 2 S", "AADDBC")
    val result = mowerService.buildMowersFromLines(validStringList)
    val expectedResult = List(
      Mower(
        PositionAndDirection(Point(1, 2), "S"),
        List("A", "A", "D", "D", "B", "C"),
        None
      )
    )
    assert(result === expectedResult)
  }

  test(
    "tryToBuildMowersFromLines should return a failure due to its wrong input"
  ) {
    val invalidStringList = List("1 1", "A 2 S", "AADDBC")
    val result = mowerService.tryToBuildMowersFromLines(invalidStringList)
    assert(result.isFailure === true)
  }

  test("moveMower should return a Mower with ") {
    val validFile = List("2 2", "0 0 E", "AAGAA")
    val mower = mowerService.buildMowersFromLines(validFile)(0)
    val result =
      mowerService.moveMower(mower.debut, mower.instructions, Point(2, 2))
    val expectedResult = PositionAndDirection(Point(2, 2), "N")
    assert(result === expectedResult)
  }

}
