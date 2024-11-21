package parsers_combinators.dibujo


sealed trait Figura
case class Triangulo(p1: Punto, p2: Punto, p3: Punto) extends Figura
case class Rectangulo(p1: Punto, p2: Punto) extends Figura
case class Circulo(centro: Punto, radio: Double) extends Figura

case class Punto(x: Double, y: Double)

sealed trait Transformacion extends Figura
case class Color(rojo: Int, verde: Int, azul: Int, figura: Figura) extends Transformacion
case class Escala(x: Double, y: Double, figura: Figura) extends Transformacion
case class Rotacion(angulo: Double, figura: Figura) extends Transformacion
case class Traslacion(dx: Double, dy: Double, figura: Figura) extends Transformacion

case class Grupo(figuras: List[Figura]) extends Figura
