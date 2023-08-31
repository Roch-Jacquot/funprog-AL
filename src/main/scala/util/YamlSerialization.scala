package util

object YamlSerialization {

  private val NEW_LINE_FOR_KEY = ":\n"
  private val ARRAY_DASH = "- "
  private val COLON = ": "

  sealed trait YamlValue extends Product with Serializable{

    /**
     * stringify transforms a YamlValue into
     * @param spacing indicates the indentation to add at the beginning of the line
     * @param isFirstLineOfArray checks whether the key is at the beginning of an array
     * @return Yaml formated String
     */
    def stringify(spacing: Int, isFirstLineOfArray: Boolean): String
  }

  final case class YamlString(value: String) extends YamlValue{
    override def stringify(spacing: Int, isFirstLineOfArray: Boolean): String = value
  }

  final case class YamlNumber(value: Int) extends YamlValue {
    override def stringify(spacing: Int, isFirstLineOfArray: Boolean): String = value.toString
  }
  
  final case class YamlArray(values: List[YamlValue]) extends YamlValue {
    override def stringify(spacing: Int, isFirstLineOfArray: Boolean): String =
      values.map {
        case yamlObject: YamlObject =>
          yamlObject.values.toList.zipWithIndex.map {
            case ((key, value), index) if index == 0 =>
              YamlObject(Map(key -> value)).stringify(spacing, isFirstLineOfArray = true)
            case yamlValueWithIndex =>
              YamlObject(Map(yamlValueWithIndex._1._1 -> yamlValueWithIndex._1._2))
                .stringify(spacing, isFirstLineOfArray = false)
          }.mkString("\n")
        case yamlValue =>
          indenter(spacing - 2) + ARRAY_DASH + yamlValue.stringify(spacing, isFirstLineOfArray = false)
      }.mkString("\n")
  }

  final case class YamlObject(values: Map[String, YamlValue]) extends YamlValue {
    override def stringify(spacing: Int, isFirstLineOfArray: Boolean): String = values.map {
      case (key, value: YamlObject) if(isFirstLineOfArray) =>
        indenter(spacing-2) + ARRAY_DASH + key + NEW_LINE_FOR_KEY +
          value.stringify(spacing + 2, isFirstLineOfArray = false)
      case (key, value: YamlObject) =>
        indenter(spacing) + key + NEW_LINE_FOR_KEY + value.stringify(spacing + 2, isFirstLineOfArray = false)
      case (key, value: YamlArray) =>
        indenter(spacing) +  key + NEW_LINE_FOR_KEY + value.stringify(spacing + 2, isFirstLineOfArray = false)
      case (key, value) =>
        indenter(spacing) + key + COLON + value.stringify(spacing, isFirstLineOfArray = false)
    }
      .mkString("\n")
  }

  /**
   * indenter is an utility function to simplify the indentation
   * @param spacing
   * @return indentation is String form
   */
  private def indenter(spacing: Int): String =
    " " * spacing

}
