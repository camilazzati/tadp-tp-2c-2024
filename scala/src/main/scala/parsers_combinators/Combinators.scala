package parsers_combinators


import scala.util.{Failure, Success, Try}
import parsers_combinators._

import scala.annotation.tailrec

class CombinatorsException(message: String) extends Exception(message)
case class ParseSuccess[+T](result: T, resto: String)

abstract class Parser[+T]{

  def apply(input: String): Try[ParseSuccess[T]]


  // OR Combinator: intenta con el primer parser, si falla, usa el segundo
  // Sintaxis: Parser1 <|> Parser2{
    // Con esta definición contemplamos que contengan un supertipo en comun
    // Ejemplo: parseInt <|> parseDouble
  def <|>[U >: T](parser2: Parser[U]): Parser[U] = input =>
       this.apply(input) match {
         case Success(success) => Success(success)
         case Failure(_) => parser2.apply(input)
       }


  // Concat Combinator: secuencia dos parsers, devolviendo los resultados como una tupla
  // Sintaxis: Parser1 <> Parser2
  // rest: resto
   def <>[U](parser2: Parser[U]): Parser[(T, U)] = input =>
       this.apply(input) match {
         case Success(ParseSuccess(result1, rest1)) => parser2.apply(rest1) match {
             case Success(ParseSuccess(result2, rest2)) => Success(ParseSuccess((result1, result2), rest2))
             case Failure(_) => Failure(new CombinatorsException("Falla segundo parser"))
           // lo envuelve en un Failure de Try
           }
         case Failure(_) => Failure(new CombinatorsException("Falla primer parser"))
       }

  // Rightmost Combinator: ejecuta dos parsers y devuelve el resultado del segundo
  // Sintaxis: (primerParser ~> segundoParser)
  def ~>[U](parser2: Parser[U]): Parser[U] = input =>
     this.apply(input) match {
       case Success(ParseSuccess(_, rest1)) => parser2.apply(rest1)
       case Failure(_) => Failure(new CombinatorsException("Falla primer parser"))
     }

  // Leftmost Combinator: ejecuta dos parsers y devuelve el resultado del primero
  // Sintaxis: (primerParser <~ segundoParser)
    // Basicamente lo que hace es (marea bastante):
    // * Verifica que devuelva solo el resultado del primer parser.
    // * Comprueba que falla si el primer o segundo parser fallan.
    // * Parsea lo primero y despues parsea lo segundo pero deja como
    // resultado el primero y luego el resto del segundo
  def <~[U](parser2: Parser[U]): Parser[T] = input =>
     this.apply(input) match {
       case Success(ParseSuccess(result1, rest1)) =>
         parser2.apply(rest1) match {
           case Success(ParseSuccess(_, rest2)) => Success(ParseSuccess(result1, rest2))
           case Failure(_) => Failure(new CombinatorsException("Falla en el segundo parser"))
         }
       case Failure(_) => Failure(new CombinatorsException("Falla en el primer parser"))
     }

  // Parser[List[T]]: parser contenido, parser separador, parser contenido, ...
  // sepBy: parsea 0 o más veces el parser de contenido separado por el parser separador
  // Retorno: Retonar una Lista con los parseos
  def sepBy[U](parser2: Parser[U]): Parser[List[T]] = input =>
      (this <~ parser2.opt()).*()(input) match {
        case Success(ParseSuccess(result, resto)) => Success(ParseSuccess(result, resto))
        case Failure(_) => Failure(CombinatorsException("Falla el combinator"))
      }

  def satisfies(condicion: T => Boolean): Parser[T] = input =>
    this.apply(input) match {
      // parsea y cumple condicion
      case Success(ParseSuccess(result, resto)) if condicion(result) => Success(ParseSuccess(result, resto))
      // parsea pero no cumple condicion
      case Success(ParseSuccess(_, _)) => Failure(CombinatorsException("Parsea pero no cumple la condicion"))
      // no parsea
      case Failure(_) => Failure(CombinatorsException("No parsea"))
    }

  // si el parser es exitoso lo aplica
  // si falla el resultado no contiene ningun valor y no consume ningun caracter
  def opt(): Parser[Option[T]] = input =>
    this.apply(input) match {
      // envuelvo el result en Some para que el tipo coincida con Option
      case Success(ParseSuccess(result, resto)) => Success(ParseSuccess(Some(result), resto))
      case Failure(_) => Success(ParseSuccess(None, input))
    }

  def *(): Parser[List[T]] = input => {
    def parserRecursivo(input: String, resultadosAcumulados: List[T]): Try[ParseSuccess[List[T]]] =
      this.apply(input) match {
        // el parser es exitoso, agrega el resultado al acumulador y vuelve a llamar recursivamente
        case Success(ParseSuccess(result, resto)) => parserRecursivo(resto, resultadosAcumulados :+ result)
        // el parser no es exitoso, devuelve el valor acumulado con el resto
        // si llegara a fallar en el primer recorrido, resultadosAcumulados estaria vacío y el input sería el del comienzo, asi que es tambien el caso base
        case Failure(_) => Success(ParseSuccess(resultadosAcumulados, input))
      }
    parserRecursivo(input, List.empty)
  }

  // * pero se tiene que aplicar al menos 1 vez
  // puedo usar * y satisfies para que se fije que el resultado de * tenga algo como para asegurarse de que se parseo al menos una vez
  def +(): Parser[List[T]] = input => {
    val kleeneParser = this.*()
    kleeneParser.satisfies(_.nonEmpty)(input) match {
      case Success(ParseSuccess(result, resto)) => Success(ParseSuccess(result, resto))
      case Failure(_) => Failure(CombinatorsException("No parseo al menos una vez"))
    }
  }

  // parsea
  // convierte el valor parseado utilizando la función
  def map[U](f: T => U): Parser[U] = input =>
    this.apply(input) match {
      case Success(ParseSuccess(resultOriginal, resto)) => Success(ParseSuccess(f(resultOriginal), resto))
      case Failure(_) => Failure(CombinatorsException("Fallo el parser"))
    }
  

}

   


