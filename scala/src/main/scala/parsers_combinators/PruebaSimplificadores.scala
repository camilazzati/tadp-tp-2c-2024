package parsers_combinators

import parsers_combinators.dibujo._

object SimplificacionTests extends App {

  def testSimplificacion(): Unit = {
    val colorAnidado = Color(255, 255, 255, Color(128, 128, 128, Circulo(Punto(0, 0), 50)))
    val rotacionAnidada = Rotacion(30, Rotacion(330, Triangulo(Punto(0, 0), Punto(1, 1), Punto(2, 0))))
    val escalaAnidada = Escala(2, 2, Escala(0.5, 0.5, Rectangulo(Punto(0, 0), Punto(4, 4))))
    val traslacionAnidada = Traslacion(10, 0, Traslacion(-10, 5, Circulo(Punto(3, 3), 10)))

    val grupoSimplificable = Grupo(List(
      Color(200, 200, 200, Triangulo(Punto(0, 0), Punto(1, 1), Punto(2, 0))),
      Color(200, 200, 200, Circulo(Punto(1, 1), 10))
    ))

    assert(SimplificadorTransformacion.simplificar(colorAnidado) == Color(128, 128, 128, Circulo(Punto(0, 0), 50)))
    assert(SimplificadorTransformacion.simplificar(rotacionAnidada) == Rotacion(0, Triangulo(Punto(0, 0), Punto(1, 1), Punto(2, 0))))
    assert(SimplificadorTransformacion.simplificar(escalaAnidada) == Escala(1, 1, Rectangulo(Punto(0, 0), Punto(4, 4))))
    assert(SimplificadorTransformacion.simplificar(traslacionAnidada) == Traslacion(0, 5, Circulo(Punto(3, 3), 10)))

    val grupoSimplificado = SimplificadorTransformacion.simplificar(grupoSimplificable)
    assert(grupoSimplificado == Color(200, 200, 200, Grupo(List(
      Triangulo(Punto(0, 0), Punto(1, 1), Punto(2, 0)),
      Circulo(Punto(1, 1), 10)
    ))))

    println("Todos los tests de simplificación pasaron correctamente.")
  }

  testSimplificacion()
}


