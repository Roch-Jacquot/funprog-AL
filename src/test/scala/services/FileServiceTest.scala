package services

import better.files.File
import model.Direction.North
import model.Instruction._
import model.{FunProgResult, Instruction, Mower, Point, PositionAndDirection}
import org.scalatest.Succeeded
import org.scalatest.funsuite.AnyFunSuite

import scala.util.{Failure, Success}

class FileServiceTest extends AnyFunSuite {

  val testFile = "src/test/resources/readFile.txt"
  val resultFileJson = "src/test/resources/result.json"
  val resultFileCsv = "src/test/resources/result.csv"
  val resultFileYaml = "src/test/resources/result.yaml"

  val fileService = FileService
  val parseAndValidateService = ParseAndValidateService
  val formatService: FormatService = FormatService()
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
  val falseFileAdress = ""

  test(
    "readLinesFromFile should return a list of string with a Try of List(\"hello\")"
  ) {
    val result = fileService.readLinesFromFile(testFile)
    val expectedResult = Success(List("hello"))
    assert(result === expectedResult)
  }
  test(
    "readLinesFromFile should return a Failure"
  ) {
    val result = fileService.readLinesFromFile(falseFileAdress)
    result match {
      case Failure(failure) =>
        assert(failure.getClass.getSimpleName === "FileOpeningException")
      case _ => fail("Should not pass through here")
    }
  }

  test("extractGardenSizeFromString should extract the gardenSize") {
    val rawGardenSize = Some("5 5")
    val result = parseAndValidateService.extractGardenSizeFromString(rawGardenSize)
    result match {
      case Success(value) => assert(value === Point(5, 5))
      case _              => fail("Should not pass through here")
    }
  }

  test("extractGardenSizeFromString should return a failure") {
    val rawGardenSize = Some("5 ")
    val result = parseAndValidateService.extractGardenSizeFromString(rawGardenSize)
    result match {
      case Failure(failure) =>
        assert(failure.getClass.getSimpleName === "GardenSizeException")
      case _ => fail("Should not pass through here")
    }
  }
  test(
    "writeJsonOutput should write a Json file for the object ResultingWork"
  ) {
    val file: File = File(resultFileJson)
    val dataToWrite = formatService.buildJsonOutput(resultingWork)
    val jsonFile = fileService.writeJsonOutput(dataToWrite, file)

    jsonFile match {
      case Success(_) => Succeeded
      case Failure(_) => fail("Should not pass through here")
    }
  }

  test(
    "writeCsvOutput should write a csv file for the object ResultingWork"
  ) {
    val file: File = File(resultFileCsv)
    val dataToWrite = formatService.buildCsvOutput(resultingWork)
    val csvFile = fileService.writeCsvOutput(dataToWrite, file)

    csvFile match {
      case Success(_) => Succeeded
      case Failure(_) => fail("Should not pass through here")
    }
  }

  test(
    "writeJsonOutput should fail to write a Json file for the object ResultingWork"
  ) {
    val file: File = File(falseFileAdress)
    val dataToWrite = formatService.buildJsonOutput(resultingWork)
    val jsonFile = fileService.writeJsonOutput(dataToWrite, file)

    jsonFile match {
      case Failure(failure) =>
        assert(failure.getClass.getSimpleName === "FileWritingException")
      case _ => fail("Should not pass through here")
    }
  }

  test(
    "writeCsvOutput should fail to write a csv file for the object ResultingWork"
  ) {
    val file: File = File(falseFileAdress)
    val dataToWrite = formatService.buildCsvOutput(resultingWork)
    val csvFile = fileService.writeCsvOutput(dataToWrite, file)

    csvFile match {
      case Failure(failure) =>
        assert(failure.getClass.getSimpleName === "FileWritingException")
      case _ => fail("Should not pass through here")
    }
  }
  test(
    "writeYamlOutput should  write a yaml file for the object ResultingWork"
  ) {
    val file: File = File(resultFileYaml)
    val dataToWrite = formatService.buildYamlOutput(resultingWork)
    val yamlFile = fileService.writeYamlOutput(dataToWrite, file)

    yamlFile match {
      case Success(_) => Succeeded
      case Failure(_) => fail("Should not pass through here")
    }
  }

  test(
    "writeYamlOutput should fail to write a csv file for the object ResultingWork"
  ) {
    val file: File = File(falseFileAdress)
    val dataToWrite = formatService.buildYamlOutput(resultingWork)
    val csvFile = fileService.writeYamlOutput(dataToWrite, file)

    csvFile match {
      case Failure(failure) =>
        assert(failure.getClass.getSimpleName === "FileWritingException")
      case _ => fail("Should not pass through here")
    }
  }

}
