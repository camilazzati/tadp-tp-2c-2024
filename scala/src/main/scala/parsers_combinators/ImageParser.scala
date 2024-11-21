package parsers_combinators

import parsers_combinators.dibujo.{Punto, Rectangulo, Triangulo, Circulo, Figura, Grupo, Color, Escala}

trait ImageParser extends BasicParsers with Combinators {
  // Parser para puntos
  private val punto: Parser[Punto] = (double <~ char('@')) <> double map {
    case (x, y) => Punto(x, y)
  }

  // Parsers para figuras
  private val triangulo: Parser[Triangulo] =
    string("triangulo[") ~> (punto.sepBy(char(','))) <~ char(']') map {
      case List(p1, p2, p3) => Triangulo(p1, p2, p3)
    }

  private val rectangulo: Parser[Rectangulo] =
    string("rectangulo[") ~> (punto <> (char(',') ~> punto)) <~ char(']') map {
      case (p1, p2) => Rectangulo(p1, p2)
    }

  private val circulo: Parser[Circulo] =
    string("circulo[") ~> (punto <> (char(',') ~> double)) <~ char(']') map {
      case (centro, radio) => Circulo(centro, radio)
    }

  // Parser general para figuras
  private val figura: Parser[Figura] = triangulo <|> rectangulo <|> circulo

  // Parser para grupos
  private val grupo: Parser[Grupo] =
    string("grupo(") ~> figura.sepBy(char(',')) <~ char(')') map Grupo

  // Parsers para transformaciones
  private val color: Parser[Color] =
    string("color[") ~> (digit.sepBy(char(',')).map {
      case List(r, g, b) => (r, g, b)
    } <> figura) <~ char(']') map {
      case ((rojo, verde, azul), fig) => Color(rojo, verde, azul, fig)
    }

  private val escala: Parser[Escala] =
    string("escala[") ~> (double <> (char(',') ~> double) <> figura) <~ char(']') map {
      case ((sx, sy), fig) => Escala(sx, sy, fig)
    }

  // Parser general para descripciones de imágenes
  val descripcionImagen: Parser[Figura] = figura <|> grupo <|> color <|> escala
}
