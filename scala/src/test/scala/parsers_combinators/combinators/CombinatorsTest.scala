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
  }
}
