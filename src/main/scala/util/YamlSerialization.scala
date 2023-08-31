package util

object YamlSerialization {

  sealed trait YamlValue extends Product with Serializable {

    /**
     * stringify transforms a YamlValue into
     * @param spacing
     *   indicates the indentation to add at the beginning of the line
     * @param isFirstLineOfArray
     *   checks whether the key is at the beginning of an array
     * @return
     *   Yaml formated String
     */
    def stringify(spacing: Int): String
  }

  final case class YamlString(value: String) extends YamlValue {
    override def stringify(spacing: Int): String = value
  }

  final case class YamlNumber(value: Int) extends YamlValue {
    override def stringify(spacing: Int): String = value.toString
  }

  final case class YamlArray(values: List[YamlValue]) extends YamlValue {
    override def stringify(spacing: Int): String = {
      val ARRAY_DASH = "- "
      val FIRST_ELEMENT = 0
      val FIRST_LINE = 1
      values
        .map {
          case yamlObject: YamlObject =>
            val yamlObjectAsList = yamlObject.values.toList
            YamlObject(
              List(
                (
                  yamlObjectAsList(FIRST_ELEMENT)._1.prependedAll(ARRAY_DASH),
                  yamlObjectAsList(FIRST_ELEMENT)._2
                )
              )
                .appendedAll(yamlObjectAsList.drop(FIRST_LINE))
                .toMap
            )
              .stringify(spacing)
          case yamlValue =>
            indenter(spacing - 2) + ARRAY_DASH + yamlValue.stringify(spacing)
        }
        .mkString("\n")
    }
  }

  final case class YamlObject(values: Map[String, YamlValue])
      extends YamlValue {
    override def stringify(spacing: Int): String = {
      val NEW_LINE_FOR_KEY = ":\n"
      val COLON = ": "

      values
        .map {
          case (key, value: YamlObject) if (key.startsWith("-")) =>
            indenter(spacing - 2) + key + NEW_LINE_FOR_KEY +
              value.stringify(spacing + 2)
          case (key, value: YamlObject) =>
            indenter(spacing) + key + NEW_LINE_FOR_KEY + value.stringify(
              spacing + 2
            )
          case (key, value: YamlArray) =>
            indenter(spacing) + key + NEW_LINE_FOR_KEY + value.stringify(
              spacing + 2
            )
          case (key, value) =>
            indenter(spacing) + key + COLON + value.stringify(spacing)
        }
        .mkString("\n")
    }
  }

  /**
   * indenter is an utility function to simplify the indentation
   * @param spacing
   * @return
   *   indentation is String form
   */
  private def indenter(spacing: Int): String =
    " " * spacing

}
