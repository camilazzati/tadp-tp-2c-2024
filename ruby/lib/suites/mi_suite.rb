# frozen_string_literal: true
require_relative '../tadspec/tad_spec'
require_relative '../persona'

class MiSuite
  include Criteria

  # TADsPec debería alcenar este mensaje
  def testear_que_leandro_tiene_22
    leandro = Persona.new('Leandro', 22)
    leandro.edad.deberia ser 22
  end

  def testear_que_falla
    leandro = Persona.new('Leandro', 22)
    leandro.edad.deberia ser 23
  end

  def testear_que_7_es_mayor_a_5
    7.deberia ser mayor_a 5
  end

  def testear_que_explota
    leandro = Persona.new('Leandro', 22)
    en { leandro.viejo? }.deberia explotar_con NoMethodError
  end

  def testear_que_leandro_entiendo_ser_viejo
    leandro = Persona.new('Leandro', 22)
    leandro.deberia entender :viejo?
  end

  def testear_que_leandro_tiene_edad
    leandro = Persona.new('Leandro', 22)
    leandro.deberia tener_edad 22
  end

  # Desde aca no debería almacenarlos
  def testear_que_leandro_tiene(edad)
    leandro = Persona.new('Leandro', 22)
    leandro.deberia tener_edad edad
  end

  def hacer_nada
    puts "Estoy haciendo nada..."
  end
end