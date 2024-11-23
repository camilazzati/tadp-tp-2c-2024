package parsers_combinators


import parsers_combinators.Parser
import parsers_combinators.dibujo._


trait ImageParser{
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

  private val rotacion: Parser[Rotacion] =
    string("rotacion[") ~> double <~ char(']') <> figura map {
      case (angulo, fig) => Rotacion(angulo % 360, fig)
    }

  private val traslacion: Parser[Traslacion] =
    string("traslacion[") ~> (double <> (char(',') ~> double) <> figura) <~ char(']') map {
      case ((dx, dy), fig) => Traslacion(dx, dy, fig)
    }

  // Parser general para descripciones de imágenes
  val descripcionImagen: Parser[Figura] =
    escala <|> rotacion <|> traslacion <|> grupo <|> figura <|> color

  // Simplificación del AST utilizando un enfoque funcional/objetual
  def simplificarAST(figura: Figura): Figura = figura match {
    case Grupo(figuras) => SimplificadorTransformacion.simplificarGrupo(figuras.map(simplificarAST))
    case t: Transformacion => SimplificadorTransformacion.simplificar(t)
    case otraFigura => otraFigura
  }
}

object SimplificadorTransformacion {
  def simplificar(figura: Figura): Figura = figura match {
    case Color(_, _, _,Color(r, g, b, fig)) => simplificar(Color(r, g, b, fig))

    case Rotacion(angulo1, Rotacion(angulo2, fig)) =>
      simplificar(Rotacion((angulo1 + angulo2) % 360.0, fig))

    case Escala(sx1, sy1, Escala(sx2, sy2, fig)) =>
      simplificar(Escala(sx1 * sx2, sy1 * sy2, fig))

    case Traslacion(dx1, dy1, Traslacion(dx2, dy2, fig)) =>
      simplificar(Traslacion(dx1 + dx2, dy1 + dy2, fig))

    case Rotacion(0, fig) => simplificar(fig)
    case Escala(1, 1, fig) => simplificar(fig)
    case Traslacion(0, 0, fig) => simplificar(fig)

    case Grupo(figuras) => simplificarGrupo(figuras)

    case Color(r, g, b, fig) => Color(r, g, b, simplificar(fig))
    case Rotacion(angulo, fig) if angulo > 359 => Rotacion(angulo % 360, simplificar(fig))
    case Escala(sx, sy, fig) => Escala(sx, sy, simplificar(fig))
    case Traslacion(dx, dy, fig) => Traslacion(dx, dy, simplificar(fig))

    case otraFigura => otraFigura
  }

  def simplificarGrupo(figuras: List[Figura]): Figura = {
    val figurasSimplificadas = figuras.map(simplificar)
    val transformacionesComunes = figurasSimplificadas.collect {
      case t: Transformacion => t
    }

    transformacionesComunes match {
      case Nil => Grupo(figurasSimplificadas)
      case Color(r, g, b, _) :: _ if figurasSimplificadas.forall {
        case Color(`r`, `g`, `b`, _) => true
        case _ => false
      } =>
        Color(r, g, b, Grupo(figurasSimplificadas.map {
          case Color(_, _, _, fig) => fig
          case otra => otra
        }))

      case _ => Grupo(figurasSimplificadas)
    }
  }
}
