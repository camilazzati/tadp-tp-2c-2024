package parsers_combinators

object PruebaImageParser extends App with ImageParser {
  val parser = new ImageParser {}
  println(parser.descripcionImagen("triangulo[0 @ 100, 200 @ 300, 150 @ 500]"))
  println(parser.descripcionImagen("color[255, 0, 0](rectangulo[0 @ 0, 100 @ 100])"))
  println(parser.descripcionImagen("grupo(triangulo[0 @ 0, 10 @ 10, 5 @ 5], circulo[5 @ 5, 3])"))

}
