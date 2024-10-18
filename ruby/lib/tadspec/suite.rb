# frozen_string_literal: true
require_relative 'resultado'
require_relative 'tad_spec'

class Suite
  attr_accessor :nombre, :tests

  def initialize(nombre)
    @nombre = nombre
    @tests = []
  end

  # Agrego a la lista el nombre del test y su bloque que representa el código a ejectuar
  def agregar_test(nombre_test, &bloque)
    @tests << {nombre: nombre_test, bloque: bloque}
  end

  def testear(*nombres_tests)
    # Convertimos los nombres de tests a strings para evitar problemas de formato
    nombres_tests = nombres_tests.map(&:to_s)

    # Filtrado, asegurándonos de que los nombres coincidan
    tests_a_ejecutar = nombres_tests.empty? ? @tests : @tests.select { |test| nombres_tests.include?(test[:nombre].to_s) }

    resultado_suite = ResultadoSuite.new(@nombre)

    tests_a_ejecutar.each do |test|
      begin
        test[:bloque].call
        resultado_suite.agregar_resultado(ResultadoExitoso.new(test))
      rescue TadspecAssertionError => e
        resultado_suite.agregar_resultado(ResultadoFallido.new(test, e))
      rescue StandardError => e
        resultado_suite.agregar_resultado(ResultadoExplotado.new(test, e))
      end
    end
    resultado_suite
  end
end
