# frozen_string_literal: true
describe "mensaje_ser" do
  include Criteria

  it 'Comparacion de numeros negando con mayor_a' do
    expect(7.deberia no ser mayor_a 10).to be(true)
  end

  it 'Comparacion de numeros negando con menor_a' do
    expect(7.deberia no ser menor_a 5).to be(true)
  end

  it 'Comparacion de numeros negando con igual_a' do
    expect(7.deberia no ser igual_a -7).to be(true)
  end

  it 'Comparacion de numeros negando con distinto_a' do
    expect(7.deberia no ser distinto_a 7).to be(true)
  end

  it 'Comparacion de cadenas negando con igual_a' do
    expect("tadp".deberia no ser igual_a "dds")
  end

  it 'Comparacion de cadena negando con distinto_a' do
    expect("tadp".deberia no ser distinto_a "tadp").to be(true)
  end

  it 'Busco en una lista una no coincidencia' do
    expect(10.deberia no ser uno_de_estos [5, 7, "tadp"]).to be(true)
  end

  it 'Busco en muchos parametros una no coincidencia' do
    expect(10.deberia no ser uno_de_estos 5, 7, "tadp").to be(true)
  end

  #Test con Clase Persona
  let(:persona_joven) { Persona.new('Pepe', 25) }
  let(:persona_vieja) { Persona.new('Pancracio', 35) }

  it 'Persona joven deberia no ser vieja' do
    expect(persona_joven.viejo?.deberia no ser true).to be(true)
  end

  it 'Persona vieja no deberia llamarse Pepe y no tiene 25 años' do
    expect(persona_vieja.nombre.deberia no ser 'Pepe').to be(true)
    expect(persona_vieja.edad.deberia no ser 25).to be(true)
  end

  it 'Persona joven no deberia llamarse Pancracio y no tiene 35 años' do
    expect(persona_joven.nombre.deberia no ser 'Pancracio').to be(true)
    expect(persona_joven.edad.deberia no ser 35).to be(true)
  end

  it 'Persona joven es mayor de edad' do
    expect(persona_joven.edad.deberia ser mayor_a 18).to be(true)
  end

  it 'Persona vieja es menor que Mirtha' do
    mirtha = Persona.new("Mirtha", 97)
    expect(persona_vieja.edad.deberia ser menor_a mirtha.edad).to be(true)
  end
end




