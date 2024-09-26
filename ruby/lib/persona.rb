# frozen_string_literal: true

class Persona
  attr_accessor :nombre, :edad

  def initialize(nombre, edad)
    @nombre = nombre
    @edad = edad
  end

  def viejo?
    @edad > 29
  end
end
