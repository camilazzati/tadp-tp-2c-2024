package parsers_combinators.basic_parsers

trait BasicParsers {
  // Tipo de resultado de un parseo
  // sealed indica que el trait solo puede ser extendido en el mismo archivo de origen donde se declara.
  sealed trait ParseResult[+T]

  case class ParseSuccess[T](result: T, resto: String) extends ParseResult[T]

  case class ParseFailure(message: String) extends ParseResult[Nothing]

  // Tipo Parser que recibe un String y devuelve un resultado de parseo
  type Parser[T] = String => ParseResult[T]

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

  // -------- OPERACIONES -------------

  // recibe un parser y una condicion, tiene que cumplir las dos cosas
  def satisfies[T](parser: Parser[T], condicion:T => Boolean): Parser[T] = input => {
    parser(input) match {
      // parsea y cumple condicion
      case ParseSuccess(result, resto) if condicion(result) => ParseSuccess(result, resto)
      // parsea pero no cumple condicion
      case ParseSuccess(_, _) => ParseFailure("El elemento parseado no cumple la condición")
      // no parsea
      case failure: ParseFailure => ParseFailure("El elemento no parsea")
    }
  }

  // si el parser es exitoso lo aplica
  // si falla el resultado no contiene ningun valor y no consume ningun caracter
  def opt[T](parser: Parser[T]): Parser[Option[T]] = input => {
    parser(input) match {
      // envuelvo el result en Some para que el tipo coincida con Option
      case ParseSuccess(result, resto) => ParseSuccess(Some(result), resto)
      case failure: ParseFailure => ParseSuccess(None, input)
    }
  }

  def *[T](parser: Parser[T]): Parser[List[T]] = input => {
    def parserRecursivo(input: String, resultadosAcumulados: List[T]): ParseSuccess[List[T]] = {
      parser(input) match {
        // el parser es exitoso, agrega el resultado al acumulador y vuelve a llamar recursivamente
        case ParseSuccess(result, resto) => parserRecursivo(resto, resultadosAcumulados :+ result )
        // el parser no es exitoso, devuelve el valor acumulado con el resto
        // si llegara a fallar en el primer recorrido, resultadosAcumulados estaria vacío y el input sería el del comienzo, asi que es tambien el caso base
        case failure: ParseFailure => ParseSuccess(resultadosAcumulados, input)
      }
    }
    parserRecursivo(input, List.empty)
  }

  // * pero se tiene que aplicar al menos 1 vez
  // puedo usar * y satisfies para que se fije que el resultado de * tenga algo como para asegurarse de que se parseo al menos una vez
  def +[T](parser:Parser[T]): Parser[List[T]] = input => {
    // TODO: hay que arreglar esto, las operaciones tendrian que ser clases como para poder aplicarse a los parsers: parser.*
    val kleeneParser = *(parser)
    satisfies(kleeneParser, _.nonEmpty)(input) match {
      case ParseSuccess(result,resto) => ParseSuccess(result, resto)
      case failure: ParseFailure => ParseFailure("No se pudo aplicar al menos 1 vez el parser")
    }
  }

  // parsea lo mismo
  // convierte el valor parseado utilizando la función
  def map[T, U](parser: Parser[T], f: T => U): Parser[U] = input => {
    parser(input) match {
      case ParseSuccess(resultOriginal, resto) => ParseSuccess(f(resultOriginal), resto)
      case failure: ParseFailure => failure
    }
  }



}