# frozen_string_literal: true
require_relative 'resultado'
require_relative 'suite'

module TADsPec
  @suites = []

  class << self
    attr_reader :suites

    def registrar_suite(suite_class)
      @suites << suite_class
    end

    def ver_suites
      @suites
    end

    def testear(suite = nil, *test_names)
      if suite.nil?
        testear_todas_las_suites
      else
        resultado_suite = testear_suite(suite, *test_names)
        resultado_suite.mostrar_resultados
      end
    end

    def testear_todas_las_suites
      resultados_totales = ResultadoTotal.new
      @suites.each do |suite_class|
        suite = crear_suite(suite_class)
        resultado_suite = suite.testear
        resultados_totales.agregar_resultado_suite(resultado_suite)
      end
      resultados_totales.mostrar_resultado_total
    end

    def testear_suite(suite_class, *test_names)
      suite_instance = suite_class.new
      suite = crear_suite(suite_instance, *test_names)
      suite.testear
    end

    def crear_suite(suite_instance, *test_names)
      suite_class = Suite.new(suite_instance.class)
      if test_names.empty?
        suite_instance.class.instance_methods(false).each do |method_test|
          if suite_instance.method(method_test).arity == 0 && method_test.to_s.start_with?("testear_que_")
            suite_class.agregar_test(method_test) { suite_instance.send(method_test) }
          end
        end
      else
        test_names.each do |method_test|
          suite_class.agregar_test(method_test) { suite_instance.send(method_test) }
        end
      end
      suite_class
    end
  end
end



  # Mensaje para correr una suite específica con tests específicos (si se pasan)
  #def self.testear_suite(suite_class, *test_names)
  #  suite = suite_class.new
  #  tests = test_names.empty? ? tests_de(suite_class) : test_names.map { |name| "testear_que_#{name}".to_sym }

  #    resultado_suite = ResultadoSuite.new(suite)
  # tests.each do |test|
  #   begin
  #     suite.send(test)
  #     resultado_suite.agregar_resultado(ResultadoExitoso.new(test))
  #   rescue TadspecAssertionError => e
  #     resultado_suite.agregar_resultado(ResultadoFallido.new(test, e))
  #   rescue StandardError => e
  #     resultado_suite.agregar_resultado(ResultadoExplotado.new(test, e))
  #
  #   end
  # end
  # resultado_suite #devuelve

#end



