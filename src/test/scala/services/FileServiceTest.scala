package services

import better.files.File
import data.{Point, ResultingWork}
import org.scalatest.funsuite.AnyFunSuite

class FileServiceTest extends AnyFunSuite {

  val testFile = "src/test/resources/readFile.txt"
  val resultFileJson = "src/test/resources/result.json"
  val resultFileCsv = "src/test/resources/result.csv"
  val fileService = FileService()

  test(
    "readLinesFromFile should return a list of string with List(\"hello\")"
  ) {
    val result = fileService.readLinesFromFile(testFile)
    val expectedResult = List("hello")
    assert(result === expectedResult)
  }

  test("extractGardenSizeFromString should extract the gardenSize") {
    val rawGardenSize = Some("5 5")
    val result = fileService.extractGardenSizeFromString(rawGardenSize)
    assert(result === Point(5, 5))
  }

  test(
    "buildJsonOutput should write a Json file for the object ResultingWork"
  ) {
    val resultingWork = ResultingWork(Point(1, 1), List.empty)
    val file: File = File(resultFileJson)
    fileService.buildJsonOutput(resultingWork, file)

    val result = fileService.readLinesFromFile(resultFileJson)

    assert(result(1).trim === "\"limite\" : {")

  }

  test("buildXmlOutput should write a csv file for the object ResultingWork") {
    val resultingWork = ResultingWork(Point(1, 1), List.empty)
    val file: File = File(resultFileCsv)
    fileService.buildCsvOutput(resultingWork, file)
    val result = fileService.readLinesFromFile(resultFileCsv)

    assert(
      result(
        0
      ).trim === "numéro;début_x;début_y;début_direction;fin_x;fin_y;fin_direction;instructions"
    )
  }

}
