package parsers_combinators

import parsers_combinators.Figura

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

    // funciona
    assert(SimplificadorTransformacion.simplificar(colorAnidado) == Color(128, 128, 128, Circulo(Punto(0, 0), 50)))
    println(SimplificadorTransformacion.simplificar(colorAnidado))
    // funciona => rotacion estaba bien pero estaba mal el test, cuando rotacion es 0 lo saca
    assert(SimplificadorTransformacion.simplificar(rotacionAnidada) == Triangulo(Punto(0.0,0.0),Punto(1.0,1.0),Punto(2.0,0.0)))
    println(SimplificadorTransformacion.simplificar(rotacionAnidada))
    // funciona => lo mismo que el anterior
    assert(SimplificadorTransformacion.simplificar(escalaAnidada) == Rectangulo(Punto(0, 0), Punto(4, 4)))
    println(SimplificadorTransformacion.simplificar(escalaAnidada))
    // este funciona
    assert(SimplificadorTransformacion.simplificar(traslacionAnidada) == Traslacion(0, 5, Circulo(Punto(3, 3), 10)))
    println(SimplificadorTransformacion.simplificar(traslacionAnidada))

    // este funciona
    val grupoSimplificado = SimplificadorTransformacion.simplificar(grupoSimplificable)
    assert(grupoSimplificado == Color(200, 200, 200, Grupo(List(
      Triangulo(Punto(0, 0), Punto(1, 1), Punto(2, 0)),
      Circulo(Punto(1, 1), 10)
    ))))
    println(SimplificadorTransformacion.simplificar(grupoSimplificable))

    println("Todos los tests de simplificación pasaron correctamente.")
  }

  testSimplificacion()
}


