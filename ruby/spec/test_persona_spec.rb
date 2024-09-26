# frozen_string_literal: true
describe 'test_personas' do
  let(:persona_joven) { Persona.new('Pepe', 25) }
  let(:persona_vieja) { Persona.new('Pancracio', 35) }

  it 'Funciona correctamente el accesor nombre' do
    expect(persona_joven.nombre).to eq('Pepe')
    expect(persona_vieja.nombre).to eq('Pancracio')
  end

  it 'Funciona correctamente el accesor edad' do
    expect(persona_joven.edad).to eq(25)
    expect(persona_vieja.edad).to eq(35)
  end

  it 'Persona joven no es vieja' do
    expect(persona_joven.viejo?).to be(false)
  end

  it 'Persona vieja es vieja' do
    expect(persona_vieja.viejo?).to be(true)
  end
end
