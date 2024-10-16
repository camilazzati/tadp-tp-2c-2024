# frozen_string_literal: true
class ResultadoTotal
  attr_reader :resultados_suites

  def initialize
    @resultados_suites = []
  end

  def agregar_resultado_suite(resultado_suite)
    @resultados_suites << resultado_suite
  end

  def mostrar_resultado_total
    @resultados_suites.each do |resultado_suite|
      resultado_suite.mostrar_resultados
    end
  end
end

class ResultadoSuite
  attr_reader :suite, :resultados

  def initialize(suite)
    @suite = suite
    @resultados = []
  end

  def agregar_resultado(resultado)
    @resultados << resultado
  end

  def mostrar_resultados
    puts "\nResultados de la suite: #{@suite}"
    @resultados.each do |resultado|
      resultado.mostrar
    end
  end
end

class ResultadoExitoso
  attr_reader :nombre

  def initialize(nombre)
    @nombre = nombre
  end

  def mostrar()
    puts("✅ #{@nombre} pasó correctamente.")
  end
end

class ResultadoFallido
  attr_reader :nombre, :error

  def initialize(nombre, error)
    @nombre = nombre
    @error = error
  end

  def mostrar()
    puts("❌ #{@nombre} falló: #{@error.message}")
  end
end

class ResultadoExplotado
  attr_reader :nombre, :error

  def initialize(nombre, error)
    @nombre = nombre
    @error = error
  end

  def mostrar
    puts("💥 #{@nombre} explotó con error: #{@error.message}\n#{@error.backtrace.join("\n")}")
  end
end


