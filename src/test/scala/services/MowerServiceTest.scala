package services

import data.Direction._
import data.Instruction._
import data.{FunProgResult, Instruction, Mower, Point, PositionAndDirection}
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Success

class MowerServiceTest extends AnyFunSuite {

  val mowerService: MowerService = MowerService()
  val parseAndValidateService = ParseAndValidateService

  val resultingWork: FunProgResult = FunProgResult(
    Point(2, 2),
    List(
      Mower(
        PositionAndDirection(Point(1, 1), North),
        List[Instruction](Forward, Right, Forward, Left),
        Some(PositionAndDirection(Point(2, 2), North))
      )
    )
  )

  test("buildMowersFromLines should return a valid list of Mowers") {
    val validStringList = List("1 1", "1 2 S", "AADDBC")
    val result = parseAndValidateService.buildMowersFromLines(validStringList)
    val expectedResult = List(
      Mower(
        PositionAndDirection(Point(1, 2), South),
        List(Forward, Forward, Right, Right, Instruction.withName("B"), Instruction.withName("C")),
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
    val expectedResult = PositionAndDirection(Point(2, 2), North)
    assert(result === expectedResult)
  }

  test("something") {
    import services.FormatService
    val formatService = FormatService()

    val result = formatService.buildYamlOutput(resultingWork)

    result.foreach(value => println(value))
  }
}
