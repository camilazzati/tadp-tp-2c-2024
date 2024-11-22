package parsers_combinators
import scala.util.Try

  // Tipo de resultado de un parseo
  // sealed indica que el trait solo puede ser extendido en el mismo archivo de origen donde se declara.
  trait ParseResult[+T]
  case class ParseSuccess[T](result: T, resto: String) extends ParseResult[T]
  case class ParseFailure(message: String) extends ParseResult[Nothing]

  // Parser que reconoce cualquier carácter
   object anyChar extends Parser[Char] {

    def apply (string:String): Try[ParseResult[Char]] =
      Try(
            if (string.nonEmpty) ParseSuccess(string.head, string.tail)
            else ParseFailure("String vacío")
      )
  }

  // Tipo Parser que recibe un String y devuelve un resultado de parseo
  //type Parser[T] = String => ParseResult[T]

  // Parser que reconoce un carácter en específico
  case class char (c:Char) extends Parser[Char] {
      def apply(string:String): Try[ParseResult[Char]] =
        Try(
          if (string.startsWith(c.toString)) ParseSuccess(c, string.tail)
          else ParseFailure(s"Se esperaba '$c', pero se encontró '${string.headOption.getOrElse("EOF")}'") // EOF: End Of File
        )
  }

  // Parser que reconoce un dígito
  object digit extends Parser[Int] {
    def apply(string:String): Try[ParseResult[Int]] =
      Try (
        if (string.nonEmpty && string.head.isDigit) ParseSuccess(string.head.asDigit, string.tail)
        else ParseFailure(s"Se esperaba un dígito, pero se encontró '${string.headOption.getOrElse("EOF")}'")
      )
  }


  // Parser que reconoce una cadena específica al principio del string
  case class string (str:String) extends  Parser[String] {
    def apply(string:String): Try[ParseResult[String]] =
      Try(
        if (string.startsWith(str)) ParseSuccess(str, string.drop(str.length))
        else ParseFailure(s"Se esperaba '$str', pero se encontró '${string.take(str.length)}'")
      )
  }


  // Parser que reconoce un número entero (puede empezar con '-')
  object integer extends Parser[Int] {
    def apply(string:String): Try[ParseResult[Int]] =
      Try {
        val pattern = "^-?\\d+".r // Expresión Regular, busca según el patrón. Utilizo Regex.
        pattern.findPrefixOf(string) match {
          case Some(numStr) => ParseSuccess(numStr.toInt, string.drop(numStr.length))
          case None => ParseFailure("Se esperaba un número entero al principio de la cadena")
        }
      }
  }

  // Parser que reconoce un número decimal
  object double extends Parser[Double] {
    def apply(string: String): Try[ParseResult[Double]] =
      Try {
        val pattern = "^-?\\d+(\\.\\d+)?".r
        pattern.findPrefixOf(string) match {
          case Some(numStr) => ParseSuccess(numStr.toDouble, string.drop(numStr.length))
          case None => ParseFailure("Se esperaba un número decimal")
        }
      }.recover {
        case _: Exception => ParseFailure("Se esperaba un número decimal")
      }
    // hay que usar recover porque el pattern puede fallar y aunque devuelva ParseFailure, no es el mismo
    // tipo de resultado esperado por el Try, que es un Try[ParseResult[Double]]
  }