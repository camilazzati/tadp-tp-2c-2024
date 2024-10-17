# frozen_string_literal: true
require_relative '../resultado'
require_relative '../tadspec/tad_spec'

class Suite
  attr_accessor :nombre, :tests

  def initialize(nombre)
    @nombre = nombre
    @tests = []
  end

  def agregar_test(nombre_test, &bloque)
    @tests << {nombre: nombre_test, bloque: bloque}
  end

  def testear(*nombres_tests)
    tests_a_ejecutar = nombres_tests.empty? ? solo_tests :  @tests.select { |test| nombres_tests.include?(test[:nombre].to_s.sub("testear_que_", "")) }
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

  def solo_tests
    @tests.select do |test|
      test[:bloque].arity == 0 && test[:nombre].to_s.start_with?("testear_que_")
    end
  end


end
