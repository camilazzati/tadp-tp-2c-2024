package parsers_combinators.combinators

import org.scalatest.freespec.AnyFreeSpec
import parsers_combinators.*

import scala.util.{Failure, Success}

class OperacionesTest extends AnyFreeSpec {
  "Operaciones combinadas" - {
    val digitParser: Parser[Int] = digit

    "satisfies" - {
      "Debería ser exitoso si cumple la condición" in {
        val satisfiesParser = digitParser.satisfies(_ > 5)
        assert(satisfiesParser("6abc") == Success(ParseSuccess(6, "abc")))
      }

      "Debería fallar si no cumple la condición" in {
        val satisfiesParser = digitParser.satisfies(_ > 5)
        assert(satisfiesParser("3abc").isInstanceOf[Failure[_]])
      }

      "Debería fallar si el parser base falla" in {
        val satisfiesParser = digitParser.satisfies(_ > 5)
        assert(satisfiesParser("abc").isInstanceOf[Failure[_]])
      }
    }

    "opt" - {
      "Debería devolver Some si el parser tiene éxito" in {
        val optParser = digitParser.opt()
        assert(optParser("7abc") == Success(ParseSuccess(Some(7), "abc")))
      }

      "Debería devolver None si el parser falla" in {
        val optParser = digitParser.opt()
        assert(optParser("abc") == Success(ParseSuccess(None, "abc")))
      }
    }

    "*" - {
      "Debería devolver una lista vacía si no puede parsear nada" in {
        val kleeneParser = digitParser.*()
        assert(kleeneParser("abc") == Success(ParseSuccess(Nil, "abc")))
      }

      "Debería devolver una lista con todos los resultados parseados" in {
        val kleeneParser = digitParser.*()
        assert(kleeneParser("123abc") == Success(ParseSuccess(List(1, 2, 3), "abc")))
      }
    }

    "+" - {
      "Debería devolver una lista con al menos un resultado" in {
        val plusParser = digitParser.+()
        assert(plusParser("123abc") == Success(ParseSuccess(List(1, 2, 3), "abc")))
      }

      "Debería fallar si no puede parsear al menos una vez" in {
        val plusParser = digitParser.+()
        assert(plusParser("abc").isInstanceOf[Failure[_]])
      }
    }

    "map" - {
      "Debería transformar el resultado del parser" in {
        val mapParser = digitParser.map(_ * 2)
        assert(mapParser("4abc") == Success(ParseSuccess(8, "abc")))
      }

      "Debería fallar si el parser original falla" in {
        val mapParser = digitParser.map(_ * 2)
        assert(mapParser("abc").isInstanceOf[Failure[_]])
      }
    }
  }
}