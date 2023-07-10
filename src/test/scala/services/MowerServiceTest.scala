package services

import data.{FunProgResult, Mower, Point, PositionAndDirection}
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Success

class MowerServiceTest extends AnyFunSuite {

  val mowerService: MowerService = MowerService()

  val resultingWork: FunProgResult = FunProgResult(
    Point(2, 2),
    List(
      Mower(
        PositionAndDirection(Point(1, 1), "N"),
        List("A", "D", "A", "G"),
        Some(PositionAndDirection(Point(2, 2), "N"))
      )
    )
  )

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
    result match {
      case Success(value) => assert(value === expectedResult)
      case _              => fail("Should not pass through here")
    }
  }

  test("moveMower should return a Mower with ") {
    val mower = resultingWork.tondeuses(0)
    val result =
      mowerService.moveMower(mower.debut, mower.instructions, Point(2, 2))
    val expectedResult = PositionAndDirection(Point(2, 2), "N")
    assert(result === expectedResult)
  }

  test("something") {
    import services.FormatService
    val formatService = FormatService()

    val result = formatService.buildYamlOutput(resultingWork)

    result.foreach(value => println(value))
  }
}
