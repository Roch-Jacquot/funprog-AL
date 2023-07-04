package util

object MyUtil {

  def outputErrorAndExit(message: String) = {
    println(message)
    sys.exit()
  }

}
