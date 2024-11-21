package parsers_combinators.combinators

import parsers_combinators.basic_parsers._
import org.scalatest.freespec.AnyFreeSpec

class OperacionesTest extends AnyFreeSpec with Combinators {
  "Operaciones combinadas" - {
    val digitParser: Parser[Int] = digit

    "satisfies" - {
      "Debería ser exitoso si cumple la condición" in {
        val satisfiesParser = digitParser.satisfies(_ > 5)
        assert(satisfiesParser("6abc") == ParseSuccess(6, "abc"))
      }

      "Debería fallar si no cumple la condición" in {
        val satisfiesParser = digitParser.satisfies(_ > 5)
        assert(satisfiesParser("3abc").isInstanceOf[ParseFailure])
      }

      "Debería fallar si el parser base falla" in {
        val satisfiesParser = digitParser.satisfies(_ > 5)
        assert(satisfiesParser("abc").isInstanceOf[ParseFailure])
      }
    }

    "opt" - {
      "Debería devolver Some si el parser tiene éxito" in {
        val optParser = digitParser.opt
        assert(optParser("7abc") == ParseSuccess(Some(7), "abc"))
      }

      "Debería devolver None si el parser falla" in {
        val optParser = digitParser.opt
        assert(optParser("abc") == ParseSuccess(None, "abc"))
      }
    }

    "*" - {
      "Debería devolver una lista vacía si no puede parsear nada" in {
        val kleeneParser = digitParser.*
        assert(kleeneParser("abc") == ParseSuccess(Nil, "abc"))
      }

      "Debería devolver una lista con todos los resultados parseados" in {
        val kleeneParser = digitParser.*
        assert(kleeneParser("123abc") == ParseSuccess(List(1, 2, 3), "abc"))
      }
    }

    "+" - {
      "Debería devolver una lista con al menos un resultado" in {
        val plusParser = digitParser.+
        assert(plusParser("123abc") == ParseSuccess(List(1, 2, 3), "abc"))
      }

      "Debería fallar si no puede parsear al menos una vez" in {
        val plusParser = digitParser.+
        assert(plusParser("abc").isInstanceOf[ParseFailure])
      }
    }

    "map" - {
      "Debería transformar el resultado del parser" in {
        val mapParser = digitParser.map(_ * 2)
        assert(mapParser("4abc") == ParseSuccess(8, "abc"))
      }

      "Debería fallar si el parser original falla" in {
        val mapParser = digitParser.map(_ * 2)
        assert(mapParser("abc").isInstanceOf[ParseFailure])
      }
    }
  }
}
