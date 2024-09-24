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

  def ser(config)
    #Si no es un config, comparo por igual
    if config.is_a? Config
      config #Se ejectua el proc
    else
      igual_a(config)
    end
  end

end

#Creamos nuestro propio Error al fallar
class TadspecAssertionError < StandardError

#Abro la clase Object para inyectarle el mensaje deberia
class Object
  def deberia(criteria)
    #Si no puede hacer el call, explota
    unless criteria.call(self)
      raise TadspecAssertionError
    end
  end
end