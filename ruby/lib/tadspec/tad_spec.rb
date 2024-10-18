# frozen_string_literal: true
require_relative 'resultado'
require_relative 'suite'

module TADsPec
  @suites = []

  class << self
    attr_reader :suites

    # Este método se ejecutará al cargar el módulo y registrará automáticamente todas las suites válidas
    def registrar_suites_automaticamente
      ObjectSpace.each_object(Class) do |klass|
        # Verificar si la clase tiene métodos válidos para ser considerada suite
        if es_una_suite?(klass)
          registrar_suite(klass)
        end
      end
    end

    # Verifica si una clase tiene al menos un método de prueba válido
    def es_una_suite?(klass)
      klass.instance_methods(false).any? do |method|
        method.to_s.start_with?("testear_que_") && klass.instance_method(method).arity == 0
      end
    end

    # Registrar manualmente una suite
    def registrar_suite(suite_class)
      @suites << suite_class unless @suites.include?(suite_class)
    end

    def ver_suites
      @suites
    end

    def testear(suite_class = nil, *test_names)
      if suite_class.nil?
        testear_todas_las_suites
      else
        resultado_suite = testear_suite(suite_class, *test_names)
        resultado_suite.mostrar_resultados
      end
    end

    def testear_todas_las_suites
      resultados_totales = ResultadoTotal.new
      @suites.each do |suite_class|
        resultado_suite = testear_suite(suite_class)
        resultados_totales.agregar_resultado_suite(resultado_suite)
      end
      resultados_totales.mostrar_resultado_total
    end

    def testear_suite(suite_class, *test_names)
      suite_instance = suite_class.new
      suite = crear_suite(suite_instance)
      suite.testear(*test_names)
    end

    def crear_suite(suite_instance)
      suite_class = Suite.new(suite_instance.class)
      suite_instance.class.instance_methods(false).each do |method_test|
        if suite_instance.method(method_test).arity == 0 && method_test.to_s.start_with?("testear_que_")
          suite_class.agregar_test(method_test) { suite_instance.send(method_test) }
        end
      end
      suite_class
    end

    def restablecer
      MockRealizados.restablecer
      Module.remove_method(:mockear)
    end
  end
end

# Registrar automáticamente las suites al cargar el módulo
TADsPec.registrar_suites_automaticamente
