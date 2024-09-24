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
    unless config.is_a? Config
      igual_a(config)
    else
      config #Se ejectua el proc
    end
  end

end

#Abro la clase Object para inyectarle el mensaje deberia
class Object
  def deberia(criteria)
    criteria.call(self)
  end
end