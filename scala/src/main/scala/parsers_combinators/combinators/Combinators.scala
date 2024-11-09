package parsers_combinators.combinators

import jdk.internal.util.xml.impl.Input
import parsers_combinators.basic_parsers.BasicParsers

trait Combinators extends BasicParsers {
  // OR Combinator: intenta con el primer parser, si falla, usa el segundo
  // Sintaxis: Parser1 <|> Parser2
  implicit class OrCombinator[T](parser1: Parser[T]) {
    // Con esta definición contemplamos que contengan un supertipo en comun
    // Ejemplo: parseInt <|> parseDouble
    def <|>[U >: T](parser2: Parser[U]): Parser[U] = input =>
      parser1(input) match {
        case success: ParseSuccess[T] => success
        case _: ParseFailure => parser2(input)
      }
  }

  // Concat Combinator: secuencia dos parsers, devolviendo los resultados como una tupla
  // Sintaxis: Parser1 <> Parser2
  implicit class ConcatCombinator[T](parser1: Parser[T]) {
    // rest: resto
    def <>[U](parser2: Parser[U]): Parser[(T, U)] = input =>
      parser1(input) match {
        case ParseSuccess(result1, rest1) =>
          parser2(rest1) match {
            case ParseSuccess(result2, rest2) => ParseSuccess((result1, result2), rest2)
            case failure: ParseFailure => failure
          }
        case failure: ParseFailure => failure
      }
  }

  // Rightmost Combinator: ejecuta dos parsers y devuelve el resultado del segundo
  // Sintaxis: (primerParser ~> segundoParser)
  implicit class RightmostCombinator[T](parser1: Parser[T]) {
    def ~>[U](parser2: Parser[U]): Parser[U] = input =>
      parser1(input) match {
        case ParseSuccess(_, rest1) => parser2(rest1)
        case failure: ParseFailure => failure
      }
  }

  // Leftmost Combinator: ejecuta dos parsers y devuelve el resultado del primero
  // Sintaxis: (primerParser <~ segundoParser)
  implicit class LeftmostCombinator[T](parser1: Parser[T]) {
    // Basicamente lo que hace es (marea bastante):
    // * Verifica que devuelva solo el resultado del primer parser.
    // * Comprueba que falla si el primer o segundo parser fallan.
    // * Parsea lo primero y despues parsea lo segundo pero deja como
    // resultado el primero y luego el resto del segundo
    def <~[U](parser2: Parser[U]): Parser[T] = input =>
      parser1(input) match {
        case ParseSuccess(result1, rest1) =>
          parser2(rest1) match {
            case ParseSuccess(_, rest2) => ParseSuccess(result1, rest2)
            case failure: ParseFailure => failure
          }
        case failure: ParseFailure => failure
      }
  }

  // TODO: sepBy no se si lo termino de entender
  // Parser[List[T]]: parser contenido, parser separador, parser contenido, ...
  // sepBy: parsea 0 o más veces el parser de contenido separado por el parser separador
  implicit class SeparatedByCombinator[T](parser1: Parser[T]) {
    def sepBy[U](parser2: Parser[U]): Parser[List[T]] = input => {
      // hace + porque minimo tiene que ser exitoso el parser1 una vez
      // opt del parser2 porque puede haber o no separador, y en caso de no haberlo devuelve solo el primer contenido
      // <~ devuelve el parser contenido, es decir List[parserContenido, parserContenido, ...], pero no estoy segura de si tambien tiene que ir el separador en el resultado
      (parser1 <~ (parser2.opt)).+ (input) match {
        case ParseSuccess(result, resto) => ParseSuccess(result, resto)
        case failure: ParseFailure => failure
      }
    }
  }




// ------------ OPERACIONES -------------

implicit class Operaciones[T](parser: Parser[T]) {


  // recibe un parser y una condicion, tiene que cumplir las dos cosas
  def satisfies(condicion: T => Boolean): Parser[T] = input =>
    parser(input) match {
      // parsea y cumple condicion
      case ParseSuccess(result, resto) if condicion(result) => ParseSuccess(result, resto)
      // parsea pero no cumple condicion
      case ParseSuccess(_, _) => ParseFailure("El elemento parseado no cumple la condición")
      // no parsea
      case failure: ParseFailure => ParseFailure("El elemento no parsea")
    }


  // si el parser es exitoso lo aplica
  // si falla el resultado no contiene ningun valor y no consume ningun caracter
  def opt: Parser[Option[T]] = input =>
    parser(input) match {
      // envuelvo el result en Some para que el tipo coincida con Option
      case ParseSuccess(result, resto) => ParseSuccess(Some(result), resto)
      case failure: ParseFailure => ParseSuccess(None, input)
    }


  def * : Parser[List[T]] = input =>
    def parserRecursivo(input: String, resultadosAcumulados: List[T]): ParseSuccess[List[T]] =
      parser(input) match {
        // el parser es exitoso, agrega el resultado al acumulador y vuelve a llamar recursivamente
        case ParseSuccess(result, resto) => parserRecursivo(resto, resultadosAcumulados :+ result)
        // el parser no es exitoso, devuelve el valor acumulado con el resto
        // si llegara a fallar en el primer recorrido, resultadosAcumulados estaria vacío y el input sería el del comienzo, asi que es tambien el caso base
        case failure: ParseFailure => ParseSuccess(resultadosAcumulados, input)
      }

    parserRecursivo(input, List.empty)


  // * pero se tiene que aplicar al menos 1 vez
  // puedo usar * y satisfies para que se fije que el resultado de * tenga algo como para asegurarse de que se parseo al menos una vez
  def + : Parser[List[T]] = input =>
    val kleeneParser = parser.*
    kleeneParser.satisfies(_.nonEmpty)(input) /*match {
          case ParseSuccess(result, resto) => ParseSuccess(result, resto)
          case failure: ParseFailure => ParseFailure("No se pudo aplicar al menos 1 vez el parser")
        }*/


  // parsea
  // convierte el valor parseado utilizando la función
  def map[U](f: T => U): Parser[U] = input =>
    parser(input) match {
      case ParseSuccess(resultOriginal, resto) => ParseSuccess(f(resultOriginal), resto)
      case failure: ParseFailure => failure
    }

}
}

