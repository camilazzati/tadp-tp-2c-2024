# frozen_string_literal: true
require_relative '../tadspec/criteria'
require_relative '../persona'

class MiSuite
  include Criteria
  # Resultados: { Pasan: 6 , Fallan: 2, Explotan: 2}

  # TADsPec debería almacenar este mensaje - Pasa
  def testear_que_leandro_tiene_22
    leandro = Persona.new('Leandro', 22)
    leandro.edad.deberia ser 22
  end

  # TADsPec debería almacenar este mensaje - Pasa
  def testear_que_leandro_es_mayor
    leandro = Persona.new('Leandro', 22)
    leandro.edad.deberia ser mayor_a 17
  end

  # TADsPec debería almacenar este mensaje - Falla
  def testear_que_falla
    leandro = Persona.new('Leandro', 22)
    leandro.edad.deberia ser 23
  end

  # TADsPec debería almacenar este mensaje - Pasa
  def testear_que_7_es_mayor_a_5
    7.deberia ser mayor_a 5
  end

  # TADsPec debería almacenar este mensaje - Pasa
  def testear_que_explota
    leandro = Persona.new('Leandro', 22)
    en { leandro.joven? }.deberia explotar_con NoMethodError
  end

  # TADsPec debería almacenar este mensaje - Pasa
  def testear_que_leandro_entiendo_ser_viejo
    leandro = Persona.new('Leandro', 22)
    leandro.deberia entender :viejo?
  end

  # TADsPec debería almacenar este mensaje - Explota
  def testear_que_deberia_explota
    7.deberia ser nada
  end

  # TADsPec debería almacenar este mensaje - Pasa
  def testear_que_leandro_tiene_edad
    leandro = Persona.new('Leandro', 22)
    leandro.deberia tener_edad 22
  end

  # TADsPec debería almacenar este mensaje - Explota
  def testear_que_deberia_explota_por_segunda_vez
    "Exploto".deberia ser boom
  end

  # TADsPec debería almacenar este mensaje - Falla
  def testear_que_leandro_tiene_edad_22_falla
    leandro = Persona.new('Leandro', 22)
    leandro.deberia no tener_edad 22
  end

  # --- Desde aca no debería almacenarlos ---
  def testear_que_leandro_tiene(edad)
    leandro = Persona.new('Leandro', 22)
    leandro.deberia tener_edad edad
  end

  def hacer_nada
    puts "Estoy haciendo nada..."
  end
end

class OtraSuite
  include Criteria
  # Resultados: { Pasan:  2, Fallan: 2, Explotan: 1}

  # TADsPec debería almacenar este mensaje - Pasa
  def testear_que_7_es_7
    7.deberia ser igual_a 7
  end

  # TADsPec debería almacenar este mensaje - Falla
  def testear_que_falla
    leandro = Persona.new('Pepe', 50)
    leandro.edad.deberia ser igual_a 51
  end

  # TADsPec debería almacenar este mensaje - Pasa
  def testear_que_7_es_mayor_a_5
    7.deberia ser mayor_a 5
  end

  # TADsPec debería almacenar este mensaje - Explota
  def testear_que_deberia_ser_explota
    7.deberia ser nada
  end

  # TADsPec debería almacenar este mensaje - Falla
  def testear_que_leandro_tiene_edad_22_falla
    leandro = Persona.new('Leandro', 22)
    leandro.deberia no tener_edad 22
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

class SuiteFuncional
  include Criteria
  # Resultados: { Pasan: 5, Fallan: 0, Explotan: 0}

  # TADsPec debería almacenar este mensaje - Pasa
  def testear_que_leandro_tiene_22
    leandro = Persona.new('Leandro', 22)
    leandro.edad.deberia ser 22
  end

  # TADsPec debería almacenar este mensaje - Pasa
  def testear_que_leandro_es_mayor
    leandro = Persona.new('Leandro', 22)
    leandro.edad.deberia ser mayor_a 17
  end

  # TADsPec debería almacenar este mensaje - Pasa
  def testear_que_explota
    leandro = Persona.new('Leandro', 22)
    en { leandro.joven? }.deberia explotar_con NoMethodError
  end

  # TADsPec debería almacenar este mensaje - Pasa
  def testear_que_leandro_entiendo_ser_viejo
    leandro = Persona.new('Leandro', 22)
    leandro.deberia entender :viejo?
  end

  # TADsPec debería almacenar este mensaje - Pasa
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

class NoSoySuite
  # TADsPec no debe tener en cuenta esta clase
  def testear_que_leandro_tiene(edad)
    leandro = Persona.new('Leandro', 22)
    leandro.deberia tener_edad edad
  end

  def hacer_nada
    puts "Estoy haciendo nada..."
  end
end