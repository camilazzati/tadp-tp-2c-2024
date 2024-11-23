package parsers_combinators

import parsers_combinators.{ParseSuccess, Parser}
import scala.util.{Failure, Try}

  // Tipo de resultado de un parseo
  // sealed indica que el trait solo puede ser extendido en el mismo archivo de origen donde se declara.
  //trait ParseResult[+T]
  //case class ParseSuccess[+T](result: T, resto: String)

class BasicParsersException(message: String) extends Exception(message)

  // Parser que reconoce cualquier carácter
  object anyChar extends Parser[Char] {
    def apply (string:String): Try[ParseSuccess[Char]] =
      Try(
        string match {
          case s if (string.nonEmpty) => ParseSuccess(string.head, string.tail)
          case s => throw new BasicParsersException("String vacío")
        }
      )
  }

  // Parser que reconoce un carácter en específico
  case class char (c:Char) extends Parser[Char] {
      def apply(string:String): Try[ParseSuccess[Char]] =
        Try(
          string match{
            case s if (string.startsWith(c.toString)) => ParseSuccess(c, string.tail)
            case s => throw new BasicParsersException(s"Se esperaba '$c', pero se encontró '${string.headOption.getOrElse("EOF")}'") // EOF: End Of File

          }
        )
  }

  // Parser que reconoce un dígito
  object digit extends Parser[Int] {
    def apply(string:String): Try[ParseSuccess[Int]] =
      Try (
        string match {
          case s if (string.nonEmpty && string.head.isDigit) => ParseSuccess (string.head.asDigit, string.tail)
          case s =>  throw new BasicParsersException(s"Se esperaba un dígito, pero se encontró '${
        string.headOption.getOrElse ("EOF")
        }'")
        }
        )
  }


  // Parser que reconoce una cadena específica al principio del string
  case class string (str:String) extends  Parser[String] {
    def apply(string:String): Try[ParseSuccess[String]] =
      Try(
        string match {
          case s if (string.startsWith (str) ) => ParseSuccess (str, string.drop (str.length) )
          case s => throw new BasicParsersException(s"Se esperaba '$str', pero se encontró '${
        string.take (str.length)
        }'")
        }
        )
  }


  // Parser que reconoce un número entero (puede empezar con '-')
  object integer extends Parser[Int] {
    def apply(string:String): Try[ParseSuccess[Int]] =
      Try {
        val pattern = "^-?\\d+".r // Expresión Regular, busca según el patrón. Utilizo Regex.
        pattern.findPrefixOf(string) match {
          case Some(numStr) => ParseSuccess(numStr.toInt, string.drop(numStr.length))
          case None => throw new BasicParsersException("Se esperaba un número entero al principio de la cadena")
        }
      }
  }

  // Parser que reconoce un número decimal
  object double extends Parser[Double] {
    def apply(string: String): Try[ParseSuccess[Double]] =
      Try {
        val pattern = "^-?\\d+(\\.\\d+)?".r
        pattern.findPrefixOf(string) match {
          case Some(numStr) => ParseSuccess(numStr.toDouble, string.drop(numStr.length))
          case None => throw new BasicParsersException("Se esperaba un número decimal")
        }
      }
     }