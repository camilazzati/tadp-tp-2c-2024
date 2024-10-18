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
    puts "----------------------------- RESULTADOS DE #{@suite} -----------------------------------"
    puts "Resultado de la suite: #{resultado_suite}\n"
    puts "Total de tests: #{cantidad}\n"
    puts "Exitosos: #{cantidad_exitosos}\n"
    puts "Fallidos: #{cantidad_fallidos}\n"
    puts "Explotados: #{cantidad_explotados}\n"
    puts "Resultados de la suite: #{@suite}\n"
    @resultados.each do |resultado|
      resultado.mostrar
    end
    puts "--------------------------------- END DE #{@suite} ---------------------------------------"
  end

  def resultado_suite
    if @resultados.any?{|resultado| resultado.is_a?(ResultadoExplotado)}
      "Test de Suite Explotado"
    elsif @resultados.any?{|resultado| resultado.is_a?(ResultadoFallido)}
      "Test de Suite Fallido"
    elsif @resultados.all?{|resultado| resultado.is_a?(ResultadoExitoso)}
      "Test de Suite Exitoso"
    end
  end

  def cantidad
    @resultados.size
  end

  def cantidad_exitosos
    @resultados.filter {|resultado| resultado.is_a?(ResultadoExitoso)}.size
  end

  def cantidad_fallidos
    @resultados.filter {|resultado| resultado.is_a?(ResultadoFallido)}.size
  end

  def cantidad_explotados
    @resultados.filter {|resultado| resultado.is_a?(ResultadoExplotado)}.size
  end

end

class ResultadoExitoso
  attr_reader :nombre

  def initialize(nombre)
    @nombre = nombre
  end

  def mostrar
    puts("✅ #{@nombre} pasó correctamente.\n")
  end
end

class ResultadoFallido
  attr_reader :nombre, :error

  def initialize(nombre, error)
    @nombre = nombre
    @error = error
  end

  def mostrar
    puts("❌ #{@nombre} falló: #{@error.message}\n")
  end
end

class ResultadoExplotado
  attr_reader :nombre, :error

  def initialize(nombre, error)
    @nombre = nombre
    @error = error
  end

  def mostrar
    puts("💥 #{@nombre} explotó con error: #{@error.message}\n#{@error.backtrace.join("\n")}\n")
  end
end


