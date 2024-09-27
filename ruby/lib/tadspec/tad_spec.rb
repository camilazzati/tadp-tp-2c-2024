# frozen_string_literal: true

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

end

#Creamos nuestro propio Error para las aserciones
class TadspecAssertionError < StandardError

end

#Abro la clase Object para inyectarle el mensaje deberia
class Object
  def deberia(criteria)
    respuesta_criteria = criteria.call(self)
    #Si no puede hacer el call, explota
    raise TadspecAssertionError unless respuesta_criteria
    respuesta_criteria
  end
end