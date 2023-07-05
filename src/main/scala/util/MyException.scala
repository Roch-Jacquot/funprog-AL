package util

object MyException {

  case class FileOpeningException(val message: String) extends Exception

  case class GardenSizeException(val message: String) extends Exception/*
  class FileOpeningException(val message: String) extends Exception
  class FileOpeningException(val message: String) extends Exception
  class FileOpeningException(val message: String) extends Exception
  class FileOpeningException(val message: String) extends Exception*/

}
