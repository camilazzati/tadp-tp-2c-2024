package parsers_combinators.basic_parsers

trait BasicParsers {
  // Tipo de resultado de un parseo
  // sealed indica que el trait solo puede ser extendido en el mismo archivo de origen donde se declara.
  sealed trait ParseResult[+T]
  case class ParseSuccess[T](result: T, resto: String) extends ParseResult[T]
  case class ParseFailure(message: String) extends ParseResult[Nothing]

  // Tipo Parser que recibe un String y devuelve un resultado de parseo
  // TODO: Analizar si debería ser privado o público :/
  private type Parser[T] = String => ParseResult[T]

  // Parser que reconoce cualquier carácter
  def anyChar: Parser[Char] = string =>
    if (string.nonEmpty) ParseSuccess(string.head, string.tail)
    else ParseFailure("String vacío")

  // Parser que reconoce un carácter en específico
  def char(c: Char): Parser[Char] = string =>
    if (string.startsWith(c.toString)) ParseSuccess(c, string.tail)
    else ParseFailure(s"Se esperaba '$c', pero se encontró '${string.headOption.getOrElse("EOF")}'") // EOF: End Of File

  // Parser que reconoce un dígito
  def digit: Parser[Int] = string =>
    if (string.nonEmpty && string.head.isDigit) ParseSuccess(string.head.asDigit, string.tail)
    else ParseFailure(s"Se esperaba un dígito, pero se encontró '${string.headOption.getOrElse("EOF")}'")

  // Parser que reconoce una cadena específica al principio del string
  def string(str: String): Parser[String] = string =>
    if (string.startsWith(str)) ParseSuccess(str, string.drop(str.length))
    else ParseFailure(s"Se esperaba '$str', pero se encontró '${string.take(str.length)}'")

  // Parser que reconoce un número entero (puede empezar con '-')
  def integer: Parser[Int] = string => {
    val pattern = "^-?\\d+".r // Expresión Regular, busca según el patrón. Utilizo Regex.
    pattern.findPrefixOf(string) match {
      case Some(numStr) => ParseSuccess(numStr.toInt, string.drop(numStr.length))
      case None => ParseFailure("Se esperaba un número entero al principio de la cadena")
    }
  }

  // Parser que reconoce un número decimal
  def double: Parser[Double] = input => {
    val pattern = "^-?\\d+(\\.\\d+)?".r
    pattern.findPrefixOf(input) match {
      case Some(numStr) => ParseSuccess(numStr.toDouble, input.drop(numStr.length))
      case None => ParseFailure("Se esperaba un número decimal")
    }
  }
}