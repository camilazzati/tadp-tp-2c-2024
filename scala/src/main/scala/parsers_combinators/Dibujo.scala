package parsers_combinators

import parsers_combinators.ASTInterpreter.interpretarFigura
import tadp.drawing.TADPDrawingAdapter
import parsers_combinators.Figura
import parsers_combinators.ImageParser
import parsers_combinators.ParseSuccess


object ASTInterpreter {

  /**
   * Interpreta una figura y la dibuja usando el TADPDrawingAdapter.
   * @param figura La figura que se desea interpretar.
   * @param adapter El adapter que se usará para dibujar.
   * @return El TADPDrawingAdapter actualizado después de procesar la figura.
   */
  def interpretarFigura(figura: Figura, adapter: TADPDrawingAdapter): TADPDrawingAdapter = figura match {
    case Triangulo(p1, p2, p3) =>
      adapter.triangle((p1.x, p1.y), (p2.x, p2.y), (p3.x, p3.y))

    case Rectangulo(p1, p2) =>
      adapter.rectangle((p1.x, p1.y), (p2.x, p2.y))

    case Circulo(centro, radio) =>
      adapter.circle((centro.x, centro.y), radio)

    case Grupo(figuras) =>
      figuras.foldLeft(adapter)((currentAdapter, figura) => interpretarFigura(figura, currentAdapter))

    case Color(r, g, b, figura) =>
      val colorAdapter = adapter.beginColor(scalafx.scene.paint.Color.rgb(r, g, b)) // Tuve que recurrir a esto
      val resultAdapter = interpretarFigura(figura, colorAdapter)
      resultAdapter.end()

    case Escala(sx, sy, figura) =>
      val scaleAdapter = adapter.beginScale(sx, sy)
      val resultAdapter = interpretarFigura(figura, scaleAdapter)
      resultAdapter.end()

    case Rotacion(angulo, figura) =>
      val rotateAdapter = adapter.beginRotate(angulo)
      val resultAdapter = interpretarFigura(figura, rotateAdapter)
      resultAdapter.end()

    case Traslacion(dx, dy, figura) =>
      val translateAdapter = adapter.beginTranslate(dx, dy)
      val resultAdapter = interpretarFigura(figura, translateAdapter)
      resultAdapter.end()
  }

  /**
   * Dibuja una imagen interpretando el AST simplificado.
   * @param figura El AST simplificado de la imagen.
   * @param outputType El tipo de salida: "screen", "image", o "interactive".
   * @param imageName Nombre del archivo si el output es "image" (opcional).
   */
  def dibujarImagen(figura: Figura, outputType: String, imageName: Option[String] = None): Unit = {
    outputType match {
      case "screen" =>
        TADPDrawingAdapter.forScreen { adapter =>
          interpretarFigura(figura, adapter)
        }

      case "image" if imageName.isDefined =>
        TADPDrawingAdapter.forImage(imageName.get) { adapter =>
          interpretarFigura(figura, adapter)
        }

      case "interactive" =>
        TADPDrawingAdapter.forInteractiveScreen { (input, adapter) =>
          Interactive(input, adapter).execute()
        }

      case _ =>
        throw new IllegalArgumentException("Tipo de salida no válido o parámetros insuficientes.")
    }
  }
}

case class Interactive(input: String, adapter: TADPDrawingAdapter){
  def execute(): Unit = {
    val parsedFigura = descripcionImagen(input) match {
      case ParseSuccess(result, _) => result
      case ParseFailure(message) =>
        throw new IllegalArgumentException(s"Error al interpretar el input: $message")
    }
    interpretarFigura(parsedFigura, adapter)
  }
}
