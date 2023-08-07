package services

import model.Direction._
import model.Instruction._
import model.{FunProgResult, Instruction, MowerAfterMovement, Point, PositionAndDirection}
import org.scalatest.funsuite.AnyFunSuite

class MowerServiceTest extends AnyFunSuite {

  val mowerService: MowerService = MowerService()
  val parseAndValidateService = ParseAndValidateService

  val resultingWork: FunProgResult = FunProgResult(
    Point(3, 3),
    List(
      MowerAfterMovement(
        PositionAndDirection(Point(1, 1), North),
        List[Instruction](Forward, Right, Forward, Left),
        PositionAndDirection(Point(2, 2), North)
      )
    )
  )

  test("moveMower should return a new valid PositionAndDirection at Point(0,0)") {
    val mower = resultingWork.tondeuses(0)
    val result =
      mowerService.moveMower(mower.debut, List[Instruction](Left, Forward, Left, Forward), resultingWork.limite)
    val expectedResult = PositionAndDirection(Point(0, 0), South)
    assert(result === expectedResult)
  }

  test("moveMower should return a new valid PositionAndDirection at Point(2, 2)") {
    val mower = resultingWork.tondeuses(0)
    val result =
      mowerService.moveMower(mower.debut, mower.instructions, resultingWork.limite)
    val expectedResult = PositionAndDirection(Point(2, 2), North)
    assert(result === expectedResult)
  }

  test("moveMower should return a new valid PositionAndDirection at Point(1, 3)") {
    val mower = resultingWork.tondeuses(0)
    val result =
      mowerService.moveMower(mower.debut, List[Instruction](Forward, Forward), resultingWork.limite)
    val expectedResult = PositionAndDirection(Point(1, 3), North)
    assert(result === expectedResult)
  }

  test("moveMower should return a new valid PositionAndDirection at Point(1, 0)") {
    val mower = resultingWork.tondeuses(0)
    val result =
      mowerService.moveMower(mower.debut, List[Instruction](Right, Right, Forward), resultingWork.limite)
    val expectedResult = PositionAndDirection(Point(1, 0), South)
    assert(result === expectedResult)
  }

  test("moveMower should return a new valid PositionAndDirection at Point(0, 1)") {
    val mower = resultingWork.tondeuses(0)
    val result =
      mowerService.moveMower(mower.debut, List[Instruction](Left, Forward), resultingWork.limite)
    val expectedResult = PositionAndDirection(Point(0, 1), West)
    assert(result === expectedResult)
  }

  test("moveMower should return a new valid PositionAndDirection at Point(3, 1)") {
    val mower = resultingWork.tondeuses(0)
    val result =
      mowerService.moveMower(mower.debut, List[Instruction](Right, Forward, Forward), resultingWork.limite)
    val expectedResult = PositionAndDirection(Point(3, 1), East)
    assert(result === expectedResult)
  }

  test("moveMower should return a new valid PositionAndDirection without changes") {
    val mower = resultingWork.tondeuses(0)
    val result =
      mowerService.moveMower(mower.debut, List[Instruction](), resultingWork.limite)
    val expectedResult = PositionAndDirection(Point(1, 1), North)
    assert(result === expectedResult)
  }

}
