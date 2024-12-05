# frozen_string_literal: true
#
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

  def no(criteria)
    Config.new(proc { | object | !criteria.call(object) })
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

  # TP INDIVIDUAL (me saque un 10 pero me corriqui que doble parametro object podria llevar a errores inesperados y dificiles de encontrar, poner un mejor nombre)
  def polimorfico_con(object)
    mensajes_a_entender = object.methods(true)
    Config.new(proc { |object|
      mensajes_a_entender.all? { |mensaje|
        object.respond_to?(mensaje)
      }
    })
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

# Abro la clase Object para inyectarle el mensaje deberia
class Object
  def deberia(criteria)
    respuesta_criteria = criteria.call(self)
    #Si no puede hacer el call, explota
    raise TadspecAssertionError unless respuesta_criteria
    respuesta_criteria
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
