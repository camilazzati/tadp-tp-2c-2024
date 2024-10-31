# frozen_string_literal: true
describe 'Testeo la funcionalidad del Spy, sin implementar el TADsPec y Criteria' do
  include Spy

  it 'Pato usa la edad' do
    pato = Persona.new("Pato", 23)
    pato = Spy.espiar(pato)

    pato.viejo?

    expect(pato.haber_recibido(:edad)).to be(true)
  end

  it 'Pato usa la edad una vez' do
    pato = Persona.new("Pato", 23)
    pato = Spy.espiar(pato)

    pato.viejo?

    expect(pato.haber_recibido(:edad).veces(1)).to be(true)
  end

  it 'Pato no usa la edad 5 veces' do
    pato = Persona.new("Pato", 23)
    pato = Spy.espiar(pato)

    pato.viejo?

    expect(pato.haber_recibido(:edad).veces(5)).to be(false)
  end

  it 'Pato usa la edad pero sin argumentos, pero se chequea que haya recibido' do
    pato = Persona.new("Pato", 23)
    pato = Spy.espiar(pato)

    pato.viejo?

    expect(pato.haber_recibido(:viejo?).con_argumentos(19, "hola")).to be(false)
  end

  it 'Pato usa la edad pero sin argumentos, pero se chequea que no haya recibido' do
    pato = Persona.new("Pato", 23)
    pato = Spy.espiar(pato)

    pato.viejo?

    expect(pato.haber_recibido(:viejo?).con_argumentos).to be(true)
  end
end
