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

end

class Object
  def deberia(criteria)
    criteria.call(self)
  end
end