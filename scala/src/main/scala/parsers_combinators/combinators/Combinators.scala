package parsers_combinators.combinators

import parsers_combinators.basic_parsers.BasicParsers

trait Combinators extends BasicParsers{
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

}
