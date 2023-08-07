package services

import model.Direction.North
import model.Instruction._
import model.{FunProgResult, Instruction, Mower, Point, PositionAndDirection}
import org.scalatest.funsuite.AnyFunSuite

class FormatServiceTest extends AnyFunSuite {

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

  val formatService: FormatService = FormatService()

  test(
    "buildJsonOutput should return a string in a Json format from resultingWork"
  ) {
    val result = formatService.buildJsonOutput(resultingWork)
    val expectedResult =
      "{\n  \"limite\" : {\n    \"x\" : 2,\n    \"y\" : 2\n  },\n  \"tondeuses\" : [ {\n" +
        "    \"debut\" : {\n      \"point\" : {\n        \"x\" : 1,\n        \"y\" : 1\n      },\n" +
        "      \"direction\" : \"N\"\n    },\n    \"instructions\" : [ \"A\", \"D\", \"A\", \"G\" ],\n" +
        "    \"fin\" : {\n      \"point\" : {\n        \"x\" : 2,\n        \"y\" : 2\n      },\n" +
        "      \"direction\" : \"N\"\n    }\n  } ]\n}"

    assert(result == expectedResult)
  }

  test(
    "buildCsvOutput should return a string in a csv format from resultingWork"
  ) {
    val result = formatService.buildCsvOutput(resultingWork)
    val expectedResult =
      "numéro;début_x;début_y;début_direction;fin_x;fin_y;fin_direction;instructions\n0;1;1;N;2;2;N;ADAG"

    assert(result == expectedResult)
  }

  test(
    "buildYamlOutput should return a string in a Json format from resultingWork"
  ) {
    val result = formatService.buildYamlOutput(resultingWork)
    val expectedResult = List(
      "---",
      "limite:",
      " x: 2",
      " y: 2",
      "tondeuses:",
      "- debut:",
      "    point:",
      "      x: 1",
      "      y: 1",
      "    direction: N",
      "  instructions:",
      "  - A",
      "  - D",
      "  - A",
      "  - G",
      "  fin:",
      "    point:",
      "      x: 2",
      "      y: 2",
      "    direction: N",
      "---"
    )
    assert(result == expectedResult)
  }

}
