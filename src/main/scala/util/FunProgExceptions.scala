package util
object FunProgExceptions {
  case class FileOpeningException(throwable: Throwable) extends Exception(throwable)
  case class GardenSizeException(throwable: Throwable) extends Exception(throwable)
  case class MowerMovementException(throwable: Throwable) extends Exception(throwable)

  case class FileWritingException(throwable: Throwable) extends Exception(throwable)
}
