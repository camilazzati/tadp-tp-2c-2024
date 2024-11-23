package parsers_combinators.combinators

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers.{a, shouldBe}
import parsers_combinators.*

import scala.util.{Failure, Success}

class CombinatorsTest extends AnyFreeSpec{
  "Combinators" - {
    "El combinador OR (<|>)" - {
      "debería devolver el resultado del primer parser si tiene éxito" in {
        val parser = char('a') <|> char('b')
        parser("abc") shouldBe Success(ParseSuccess('a', "bc"))
      }

      "debería intentar el segundo parser si el primero falla" in {
        val parser = char('x') <|> char('b')
        parser("bca") shouldBe Success(ParseSuccess('b', "ca"))
      }

      "debería fallar si ambos parsers fallan" in {
        val parser = char('x') <|> char('y')
        parser("abc") shouldBe a[Failure[_]]
      }

      "debería devolver el resultado del primer parser si tiene éxito - Con Integer" in {
        val parser = integer <|> double
        parser("123abc") shouldBe Success(ParseSuccess(123, "abc"))
      }

      "debería devolver el resultado del primer parser si tiene éxito - Con Double" in {
        val parser = double <|> integer
        parser("123.456abc") shouldBe Success(ParseSuccess(123.456, "abc"))
      }

      "debería fallar si el input no es un número válido" in {
        val parser = integer <|> double
        parser("TADP") shouldBe a[Failure[_]]
      }
    }

    "El combinador de concatenación (<>):" - {
      "debería devolver una tupla con los resultados de ambos parsers en caso de éxito" in {
        val parser = char('a') <> char('b')
        parser("abc") shouldBe Success(ParseSuccess(('a', 'b'), "c"))
      }

      "debería devolver una tupla con los resultados de ambos parsers en caso de éxito - Con Strings" in {
        // Hay qye tener cuidado con los espacios :/
        val parser = string("Hola ") <> string("mundo")
        parser("Hola mundo") shouldBe Success(ParseSuccess(("Hola ", "mundo"), ""))
      }

      "debería fallar si el primer parser falla" in {
        val parser = char('x') <> char('b')
        parser("abc") shouldBe a[Failure[_]]
      }

      "debería fallar si el segundo parser falla" in {
        val parser = char('a') <> char('x')
        parser("abc") shouldBe a[Failure[_]]
      }
    }

    "El combinador rightmost (~>):" - {
      "debería devolver solo el resultado del segundo parser en caso de éxito" in {
        val parser = char('a') ~> char('b')
        parser("abc") shouldBe Success(ParseSuccess('b', "c"))
      }

      "integer ~> char" in {
        val parser = integer ~> char('a')
        parser("123abc") shouldBe Success(ParseSuccess('a', "bc"))
      }

      "double ~> char" in {
        val parser = double ~> char('a')
        parser("123.456abc") shouldBe Success(ParseSuccess('a', "bc"))
      }

      "integer ~> string" in {
        val parser = integer ~> string("abc")
        parser("123abc") shouldBe Success(ParseSuccess("abc", ""))
      }

      "double ~> string" in {
        val parser = double ~> string("abc")
        parser("123.456abc") shouldBe Success(ParseSuccess("abc", ""))
      }

      "debería fallar si el primer parser falla" in {
        val parser = char('x') ~> char('b')
        parser("abc") shouldBe a[Failure[_]]
      }

      "debería fallar si el segundo parser falla" in {
        val parser = char('a') ~> char('x')
        parser("abc") shouldBe a[Failure[_]]
      }
    }

    "El combinador leftmost (<~):" - {
      "debería devolver solo el resultado del primer parser en caso de éxito" in {
        val parser = char('a') <~ char('b')
        parser("abc") shouldBe Success(ParseSuccess('a', "c"))
      }

      "debería devolver solo el resultado del primer parser en caso de éxito - Con Strings" in {
        // Sirve para eliminar :o
        val parser = string("TADP") <~ string(" X")
        parser("TADP X") shouldBe Success(ParseSuccess("TADP", ""))
      }

      "debería fallar si el primer parser falla" in {
        val parser = char('x') <~ char('b')
        parser("abc") shouldBe a[Failure[_]]
      }

      "debería fallar si el segundo parser falla" in {
        val parser = char('a') <~ char('x')
        parser("abc") shouldBe a[Failure[_]]
      }
    }

    "El cominador sepBy:" - {
      "debería devolver el resultado exitoso" in {
        val parserNumeroDeTelefono = integer.sepBy(char('-'))
        parserNumeroDeTelefono("1234-5678") shouldBe Success(ParseSuccess(List(1234, 5678) , ""))
      }
      "debería devolver el resultado exitoso pero con resto" in {
        val parserNumeroDeTelefono = integer.sepBy(char('-'))
        parserNumeroDeTelefono("1234 5678") shouldBe Success(ParseSuccess(List(1234), " 5678"))
      }
      "debería devolver el resultado fallido" in {
        val parserNumeroDeTelefono = integer.sepBy(char('-'))
        parserNumeroDeTelefono("abcd-5678") shouldBe a[Failure[_]]
      }
      "debería devolver el resultado exitoso con strings" in {
        val parserString = string("hola").sepBy(char(',')) // Acá tira el error
        parserString("hola,mundo") shouldBe Success(ParseSuccess(List("hola"), "mundo"))
      }
    }

  }
}