# frozen_string_literal: true
class Test

  def initialize(nombre, block)
    @nombre = nombre
    @block = block
  end

  def correr
    @block.call
    #correrlo y guardar el resultado
  end

end

class TestSuite
  def initialize(nombre)
    @nombre = nombre
    @tests = []
  end

  def agregar_test(nombre, &block)
    @tests << Test.new(nombre, block)
  end

  def correr
    @tests.map(&:correr)
  end

end


class TestResultado
  attr_reader :nombre, :estado, :error

  def initialize(nombre, estado, error = nil)
    @nombre = nombre
    @estado = estado
    @error = error
  end


end