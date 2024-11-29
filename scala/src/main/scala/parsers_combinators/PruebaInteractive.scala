package parsers_combinators

object PruebaInteractive extends App {

  /*val figuraPrueba = Grupo(List(
    Color(255, 0, 0, Circulo(Punto(100, 100), 50)), // Círculo rojo
    Rotacion(45, Rectangulo(Punto(50, 50), Punto(150, 150))), // Rectángulo rotado
    Escala(2, 2, Triangulo(Punto(200, 200), Punto(250, 250), Punto(300, 200))) // Triángulo escalado
  ))*/

  val figuraPrueba = Grupo(List(
      Color(255, 0, 0, Circulo(Punto(500, 100), 50)),
      Color(255, 0, 0, Circulo(Punto(300, 100), 50)),
      Color(0, 255, 0, Triangulo(Punto(400, 200), Punto(350, 250), Punto(450, 250))),
      Rectangulo(Punto(300, 350), Punto(500, 370)),
      Rectangulo(Punto(300, 310), Punto(320, 350)),
      Rectangulo(Punto(480, 310), Punto(500, 350))
  ))

  val corazon = Grupo(List(
    Color(200, 0, 0,
      Grupo(List(
        Escala(1, 0.8,
          Grupo(List(
            Circulo(Punto(210, 250), 100),
            Circulo(Punto(390, 250), 100)
          ))),
        Rectangulo(Punto(200, 170), Punto(400, 300)),
        Triangulo(Punto(113, 225), Punto(487, 225), Punto(300, 450))
      ))
    )
  ))
  
  val pepita = Grupo(List(
      Color(0, 0, 80,
        Grupo(List(
          Triangulo(Punto(50, 400), Punto(250, 400), Punto(200, 420)),
          Triangulo(Punto(50, 440), Punto(250, 440), Punto(200, 420))
        ))),
      Color(150, 150, 150,
        Triangulo(Punto(200, 420), Punto(250, 400), Punto(250, 440))),
      Color(180, 180, 160,
        Triangulo(Punto(330, 460), Punto(250, 400), Punto(250, 440))),
      Color(200, 200, 180,
        Grupo(List(
          Triangulo(Punto(330, 460), Punto(400, 400), Punto(330, 370)),
          Triangulo(Punto(330, 460), Punto(400, 400), Punto(370, 450)), 
          Triangulo(Punto(400, 430), Punto(400, 400), Punto(370, 450)),
          Triangulo(Punto(330, 460), Punto(250, 400), Punto(330, 370))
        ))),
      Grupo(List(
        Color(150, 0, 0,
          Grupo(List(
            Triangulo(Punto(430, 420), Punto(400, 400), Punto(450, 400)),
            Triangulo(Punto(430, 420), Punto(400, 400), Punto(400, 430))
          ))),
        Color(100, 0, 0, Triangulo(Punto(420, 420), Punto(420, 400), Punto(400, 430))),
        Color(0, 0, 60,
          Grupo(List(
            Triangulo(Punto(420, 400), Punto(400, 400), Punto(400, 430)),
            Triangulo(Punto(420, 380), Punto(400, 400), Punto(450, 400)),
            Triangulo(Punto(420, 380), Punto(400, 400), Punto(300, 350))
          ))),
        Color(150, 150, 0, Triangulo(Punto(440, 410), Punto(440, 400), Punto(460, 400)))
      )),
      Color(0, 0, 60,
        Grupo(List(
          Triangulo(Punto(330, 300), Punto(250, 400), Punto(330, 370)),
          Triangulo(Punto(330, 300), Punto(400, 400), Punto(330, 370)),
          Triangulo(Punto(360, 280), Punto(400, 400), Punto(330, 370)),
          Triangulo(Punto(270, 240), Punto(100, 220), Punto(330, 370)),
          Triangulo(Punto(270, 240), Punto(360, 280), Punto(330, 370))
        ))
      )
    )
  )

  //ASTInterpreter.dibujarImagen(corazon, "image", Some("corazon.png"))
  //ASTInterpreter.dibujarImagen(figuraPrueba, "image", Some("figuraPrueba.png"))
  ASTInterpreter.dibujarImagen(pepita, "image", Some("pepita.png"))

}
