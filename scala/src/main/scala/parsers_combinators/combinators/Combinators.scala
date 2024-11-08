package parsers_combinators.combinators

import parsers_combinators.basic_parsers.BasicParsers

trait Combinators extends BasicParsers{
  // OR Combinator: intenta con el primer parser, si falla, usa el segundo
  implicit class OrCombinator[T](parser1: Parser[T]) {
    // Con esta definición contemplamos que contengan un supertipo en comun
    // Ejemplo: parseInt <|> parseDouble
    def <|>[U >: T](parser2: Parser[U]): Parser[U] = input =>
      parser1(input) match {
        case success: ParseSuccess[T] => success
        case _: ParseFailure => parser2(input)
      }
  }

}
