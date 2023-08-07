package services

import model.Direction._
import model.Instruction._
import model.{Instruction, MowerAtStart, Point, PositionAndDirection}
import org.scalatest.funsuite.AnyFunSuite

import scala.util.{Failure, Success}

class ParseAndValidateServiceTest extends AnyFunSuite {

  val parseAndValidateService = ParseAndValidateService

  test("extractGardenSizeFromString should extract the gardenSize") {
    val rawGardenSize = Some("5 5")
    val result = parseAndValidateService.extractGardenSizeFromString(rawGardenSize)
    result match {
      case Success(value) => assert(value === Point(5, 5))
      case _ => fail("Should not pass through here")
    }
  }

  test("extractGardenSizeFromString should return a failure due to a missing value") {
    val rawGardenSize = Some("5 ")
    val result = parseAndValidateService.extractGardenSizeFromString(rawGardenSize)
    result match {
      case Failure(failure) =>
        assert(failure.getClass.getSimpleName === "GardenSizeException")
      case _ => fail("Should not pass through here")
    }
  }
  test("extractGardenSizeFromString should return a failure due to an invalid value") {
    val rawGardenSize = Some("5 X")
    val result = parseAndValidateService.extractGardenSizeFromString(rawGardenSize)
    result match {
      case Failure(failure) =>
        assert(failure.getClass.getSimpleName === "GardenSizeException")
      case _ => fail("Should not pass through here")
    }
  }

  test("extractGardenSizeFromString should return a failure due to a negative number") {
    val rawGardenSize = Some("5 -5")
    val result = parseAndValidateService.extractGardenSizeFromString(rawGardenSize)
    result match {
      case Failure(failure) =>
        assert(failure.getClass.getSimpleName === "GardenSizeException")
      case _ => fail("Should not pass through here")
    }
  }

  test("buildMowersFromLines should return a MowerParsingException due to wrong instructions") {
    val inValidStringList = List("1 2 S", "AADDBC")
    val result = parseAndValidateService.buildMowersFromLines(inValidStringList)
    result match {
      case Failure(failure) => assert(failure.getClass.getSimpleName === "MowerParsingException")
      case _ => fail("Should not pass through here")
    }
  }

  test("buildMowersFromLines should return a valid list of one mower") {
    val ValidStringList = List("1 2 S", "AADD")
    val result = parseAndValidateService.buildMowersFromLines(ValidStringList)
    val expectedResult = List(
      MowerAtStart(
        PositionAndDirection(Point(1, 2), South),
        List[Instruction](Forward, Forward, Right, Right)
      )
    )
    result match {
      case Success(success) => assert(success === expectedResult)
      case _ => fail("Should not pass through here")
    }
  }

  test("buildMowersFromLines should return a MowerParsingException due to a wrong direction") {
    val inValidStringList = List("1 2 B", "AADD")
    val result = parseAndValidateService.buildMowersFromLines(inValidStringList)
    result match {
      case Failure(failure) => assert(failure.getClass.getSimpleName === "MowerParsingException")
      case _ => fail("Should not pass through here")
    }
  }

  test("buildMowersFromLines should return a MowerParsingException due to a negative starting point") {
    val inValidStringList = List("1 -2 S", "AADD")
    val result = parseAndValidateService.buildMowersFromLines(inValidStringList)
    result match {
      case Failure(failure) => assert(failure.getClass.getSimpleName === "MowerParsingException")
      case _ => fail("Should not pass through here")
    }
  }

}
