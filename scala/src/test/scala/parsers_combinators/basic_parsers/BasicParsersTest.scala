package parsers_combinators.basic_parsers

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class BasicParsersTest extends AnyFreeSpec with BasicParsers {
  "BasicParsers" - {

    "anyChar" - {
      "Parsea correctamente el primer caracter de hola" in {
        anyChar("hola") shouldBe ParseSuccess('h', "ola")
      }
      "Falla el Parseo al recibir un string vacio" in {
        anyChar("") shouldBe ParseFailure("String vacío")
      }
    }

    "char" - {
      "Parsea correctamente" in {
        char('u')("utn") shouldBe ParseSuccess('u', "tn")
      }
      "Falla el Parseo por pasarle un caracter incorrecto" in {
        char('g')("perro") shouldBe ParseFailure("Se esperaba 'g', pero se encontró 'p'")
      }
      "Falla el Parseo por recibir una cadena vacia" in {
        char('c')("") shouldBe ParseFailure("Se esperaba 'c', pero se encontró 'EOF'")
      }
    }

    "digit" - {
      "Parsea correctamente con digit" in {
        digit("5abc") shouldBe ParseSuccess(5, "abc")
      }
      "Falla el Parseo porque no comienza con un digito" in {
        digit("abc") shouldBe ParseFailure("Se esperaba un dígito, pero se encontró 'a'")
      }
      "Falla el Parseo porque se le envia una cadena vacia" in {
        digit("") shouldBe ParseFailure("Se esperaba un dígito, pero se encontró 'EOF'")
      }
    }

    "string" - {
      "Parsea correctamente con string" in {
        string("hola")("hola mundo!") shouldBe ParseSuccess("hola", " mundo!")
      }
      "Falla el parseo por buscar una cadena que no existe" in {
        string("hola")("chau mundo!") shouldBe ParseFailure("Se esperaba 'hola', pero se encontró 'chau'")
      }
    }

    "integer" - {
      "Parsea correctamente con integer" in {
        integer("123abc") shouldBe ParseSuccess(123, "abc")
      }
      "Parsea correctamente con integer recibiendo un numero negativo" in {
        integer("-456xyz") shouldBe ParseSuccess(-456, "xyz")
      }
      "Falla el Parseo porque no recibe un numero entero al principio de la cadena" in {
        integer("abc123") shouldBe ParseFailure("Se esperaba un número entero al principio de la cadena")
      }
    }

    "double" - {
      "Parsea correctamente con double" in {
        double("123.45abc") shouldBe ParseSuccess(123.45, "abc")
      }
      "Parsea correcatemente un decimal negativo" in {
        double("-678.90xyz") shouldBe ParseSuccess(-678.90, "xyz")
      }
      "Falla el Parseo porque no recibe un numero decimal al principio de la cadena" in {
        double("abc123.45") shouldBe ParseFailure("Se esperaba un número decimal")
      }
    }
  }
}
