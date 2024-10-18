# frozen_string_literal: true
require_relative '../tadspec/criteria'
require_relative '../tadspec/espiar'
require_relative '../persona'

class SuitePersona
  include Criteria
  include Spy

  def testear_que_se_use_la_edad
    pato = Persona.new("Pato", 23)
    pato = Spy.espiar(pato)

    pato.viejo?

    pato.deberia haber_recibido(:edad)  # Pasa: el método `edad` fue llamado
  end

  def testear_que_pato_use_la_edad_1_vez
    pato = Persona.new("Pato", 23)
    pato = Spy.espiar(pato)

    pato.viejo?

    pato.deberia haber_recibido(:edad).veces(1)  # Pasa: `edad` se llamó una vez
  end

  def testear_que_pato_no_use_la_edad_mas_de_5_veces
    pato = Persona.new("Pato", 23)
    pato = Spy.espiar(pato)

    pato.viejo?

    pato.deberia no haber_recibido(:edad).veces(5)  # Falla: se esperaba 5 veces, pero fue 1 vez
  end

end

