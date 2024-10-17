# frozen_string_literal: true
require_relative '../resultado'


module Criteria
  class Config
    def initialize(proc)
      @proc = proc
    end

    def call(object)
      @proc.call(object)
    end
  end

  def igual_a(algo)
    Config.new(proc { |object| object == algo })
  end

  def distinto_a(algo)
    Config.new(proc {|object| object != algo})
  end

  def mayor_a(algo)
    Config.new(proc {|object| object > algo})
  end

  def menor_a(algo)
    Config.new(proc {|object| object < algo})
  end

  def uno_de_estos(*cosas)
    # *cosas captura todos los argumentos pasados al mensaje
    # si es un array lo trata como tal y si es una lista de argumentos tambien
    cosas = cosas.first if cosas.first.is_a?(Array)
    Config.new(proc { |object| cosas.include? object })
  end

  def ser(config)
    #Si no es un config, comparo por igual
    if config.is_a? Config
      config #Se ejectua el proc
    else
      igual_a(config)
    end
  end

  def no( criteria )
    proc { | object | !criteria.call(object) }
  end

  def entender(mensaje)
    Config.new(proc { |object|
      entiende = object.respond_to?(mensaje, true)

      # si el objeto no entiende el mensaje, tira error
      unless entiende
        raise EntenderError, "El objeto #{object} no entiende el mensaje :#{mensaje}"
      end

      # si lo entiende retorna true,
      entiende
    })
  end

  def en(&bloque)
    bloque
  end

  def explotar_con(error_esperado)
    Config.new(proc { |object|
      begin
        object.call

        # si no tira error => falla
        raise WrongError, "Se esperaba que explote con #{error_esperado}, pero no exploto "

      rescue Exception => error_obtenido
        # si el error es el esperado => pasa
        if error_obtenido.is_a?(error_esperado)
          true
        else # si tira error pero no es el esperado => falla
          raise WrongError, "Se esperaba que explote con #{error_esperado}, pero exploto con #{error_obtenido.class}"
        end

      end

    })
  end

  def tener_(mensaje, *args)
    proc { |object|
      if object.instance_variable_defined?("@#{mensaje}")
        valor_actual = obtener_valor_variable(object, mensaje)

        if args.first.is_a?(Config)
          # Si args.first es un Config, evaluamos el criterio con el valor actual
          args.first.call(valor_actual)
        else
          # Si args.first no es un Config, comparamos el valor directamente
          valor_esperado = args.first
          valor_actual == valor_esperado
        end
      else
        raise NoVariableError, NoVariableError.mensajeError(object, mensaje)
      end
    }
  end


  def ser_(mensaje, *args)
    proc{ |object|
      if object.respond_to?("#{mensaje}?")
        object.send("#{mensaje}?")
      else
        raise NoMethodError, NoMethodError.mensajeError(object, mensaje)
      end
    }
  end

  private
  def obtener_valor_variable(object, mensaje)
    variable = "@#{mensaje}"

    unless object.instance_variable_defined?(variable)
      raise NoVariableError, NoVariableError.mensajeError(object, mensaje)
    end

    object.instance_variable_get(variable)
  end

  def method_missing(method_name, *args, &block)
    if respond_to_missing?(method_name)
      msg = method_name.to_s.split('_').last.to_sym
      if method_name.to_s.start_with? 'tener_'
        tener_(msg, *args)
      else method_name.to_s.start_with? 'ser_'
        ser_(msg, *args)
      end
    else
      super
    end
  end

  def respond_to_missing?(mensaje, include_private = false)
    mensaje.to_s.start_with?('tener_') || mensaje.to_s.start_with?('ser_') || super
  end
end

#Creamos nuestro propio Error para las aserciones
class TadspecAssertionError < StandardError
end

class EntenderError < StandardError
end

class WrongError < StandardError
end

class NoVariableError < StandardError
  def self.mensajeError(object, mensaje)
    "#{object} no tiene el atributo :#{mensaje}"
  end
end

class NoMethodError
  def self.mensajeError(object, mensaje)
    "#{object} no entiende el mensaje :#{mensaje}"
  end
end

# Abro la clase Object para inyectarle el mensaje deberia
class Object
  def deberia(criteria)
    respuesta_criteria = criteria.call(self)
    #Si no puede hacer el call, explota
    raise TadspecAssertionError unless respuesta_criteria
    respuesta_criteria
  end
end

module TADsPec
  # Se guardan todos los suites
  @suites = []


  # Mensaje para registrar una suite de tests
  def self.registrar_suite(suite_class)
    @suites << suite_class

  end

  def self.ver_suites
    @suites
  end

  # Mensaje para obtener todos los tests de una suite
  def self.tests_de(suite_class)
    # Me fijo en sus metodos de instancia, primero verifico que tenga aridad 0 y despues que empiece con testear_que_
    suite_class.instance_methods.select do |method|
      suite_class.instance_method(method).arity == 0 && method.to_s.start_with?("testear_que_")
    end
  end

  # Mensaje para correr todos los tests de una suite o tests específicos
  def self.testear(suite_class = nil, *test_names)
    #resultados = { total: 0, pasados: 0, fallidos: 0, explotados: 0, detalles: [] }
    # Si el suite_class es null, se debe ejecutar todos los suites
    if suite_class.nil?
      testear_todas_las_suites
    else
      resultado_suite = testear_suite(suite_class,*test_names)
      resultado_suite.mostrar_resultados
    end
  end

  # Mensaje para correr todas las suites guardadas
  def self.testear_todas_las_suites
    resultados_totales = ResultadoTotal.new
    @suites.each do |suite|
      resultado_suite = testear_suite(suite)
      resultados_totales.agregar_resultado_suite(resultado_suite)
    end
    resultados_totales.mostrar_resultado_total
  end

  # Mensaje para correr una suite específica con tests específicos (si se pasan)
  def self.testear_suite(suite_class, *test_names)
    suite = suite_class.new
    tests = test_names.empty? ? tests_de(suite_class) : test_names.map { |name| "testear_que_#{name}".to_sym }

    resultado_suite = ResultadoSuite.new(suite)
    tests.each do |test|
      begin
        suite.send(test)
        resultado_suite.agregar_resultado(ResultadoExitoso.new(test))
      rescue StandardError => e
        resultado_suite.agregar_resultado(ResultadoExplotado.new(test, e))
      rescue TadspecAssertionError => e
        resultado_suite.agregar_resultado(ResultadoFallido.new(test, e))
      end
    end
    resultado_suite #devuelve
  end
end


