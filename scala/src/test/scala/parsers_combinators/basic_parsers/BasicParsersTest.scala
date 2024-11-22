import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers.{a, shouldBe}
import parsers_combinators.*

import scala.util.{Failure, Success}

class BasicParsersTest extends AnyFreeSpec {
  "BasicParsers" - {

    "anyChar" - {
      "Parsea correctamente el primer caracter de hola" in {
        anyChar.apply("hola") shouldBe Success(ParseSuccess('h', "ola"))
      }
      "Falla el Parseo al recibir un string vacio" in {
        anyChar.apply("") shouldBe a[Failure[_]]
      }
    }

    "char" - {
      "Parsea correctamente" in {
        char.apply('u')("utn") shouldBe Success(ParseSuccess('u', "tn"))
      }
      "Falla el Parseo por pasarle un caracter incorrecto" in {
        char.apply('g')("perro") shouldBe a[Failure[_]]
      }
      "Falla el Parseo por recibir una cadena vacia" in {
        char('c')("") shouldBe a[Failure[_]]
      }
    }

    "digit" - {
      "Parsea correctamente con digit" in {
        digit("5abc") shouldBe Success(ParseSuccess(5, "abc"))
      }
      "Falla el Parseo porque no comienza con un digito" in {
        digit("abc") shouldBe a[Failure[_]]
      }
      "Falla el Parseo porque se le envia una cadena vacia" in {
        digit("") shouldBe a[Failure[_]]
      }
    }

    "string" - {
      "Parsea correctamente con string" in {
        string("hola")("hola mundo!") shouldBe Success(ParseSuccess("hola", " mundo!"))
      }
      "Falla el parseo por buscar una cadena que no existe" in {
        string("hola")("chau mundo!") shouldBe a[Failure[_]]
      }
    }

    "integer" - {
      "Parsea correctamente con integer" in {
        integer("123abc") shouldBe Success(ParseSuccess(123, "abc"))
      }
      "Parsea correctamente con integer recibiendo un numero negativo" in {
        integer("-456xyz") shouldBe Success(ParseSuccess(-456, "xyz"))
      }
      "Falla el Parseo porque no recibe un numero entero al principio de la cadena" in {
        integer("abc123") shouldBe a[Failure[_]]
      }
    }

    "double" - {
      "Parsea correctamente con double" in {
        double("123.45abc") shouldBe Success(ParseSuccess(123.45, "abc"))
      }
      "Parsea correcatemente un decimal negativo" in {
        double("-678.90xyz") shouldBe Success(ParseSuccess(-678.90, "xyz"))
      }
      "Falla el Parseo porque no recibe un numero decimal al principio de la cadena" in {
        double("abc123.45") shouldBe a[Failure[_]]
      }
    }
  }
}
