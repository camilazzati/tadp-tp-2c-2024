package parsers_combinators.combinators

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers.{a, shouldBe}

class CombinatorsTest extends AnyFreeSpec with Combinators {
  "Combinators" - {
    "El combinador OR (<|>)" - {
      "debería devolver el resultado del primer parser si tiene éxito" in {
        val parser = char('a') <|> char('b')
        parser("abc") shouldBe ParseSuccess('a', "bc")
      }

      "debería intentar el segundo parser si el primero falla" in {
        val parser = char('x') <|> char('b')
        parser("bca") shouldBe ParseSuccess('b', "ca")
      }

      "debería fallar si ambos parsers fallan" in {
        val parser = char('x') <|> char('y')
        parser("abc") shouldBe a[ParseFailure]
      }

      "debería devolver el resultado del primer parser si tiene éxito - Con Integer" in {
        val parser = integer <|> double
        parser("123abc") shouldBe ParseSuccess(123, "abc")
      }

      "debería devolver el resultado del primer parser si tiene éxito - Con Double" in {
        val parser = double <|> integer
        parser("123.456abc") shouldBe ParseSuccess(123.456, "abc")
      }

      "debería fallar si el input no es un número válido" in {
        val parser = integer <|> double
        parser("TADP") shouldBe a[ParseFailure]
      }
    }

    "El combinador de concatenación (<>):" - {
      "debería devolver una tupla con los resultados de ambos parsers en caso de éxito" in {
        val parser = char('a') <> char('b')
        parser("abc") shouldBe ParseSuccess(('a', 'b'), "c")
      }

      "debería devolver una tupla con los resultados de ambos parsers en caso de éxito - Con Strings" in {
        // Hay qye tener cuidado con los espacios :/
        val parser = string("Hola ") <> string("mundo")
        parser("Hola mundo") shouldBe ParseSuccess(("Hola ", "mundo"), "")
      }

      "debería fallar si el primer parser falla" in {
        val parser = char('x') <> char('b')
        parser("abc") shouldBe a[ParseFailure]
      }

      "debería fallar si el segundo parser falla" in {
        val parser = char('a') <> char('x')
        parser("abc") shouldBe a[ParseFailure]
      }
    }

  }
}
