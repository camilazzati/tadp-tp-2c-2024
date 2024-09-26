describe "mensaje_ser" do
  include Criteria

  it "Comparacion de numeros con mayor_a" do
    expect(7.deberia ser mayor_a 5).to be(true)
  end

  it "Comparacion de numeros con menor_a" do
    expect(7.deberia ser menor_a 10).to be(true)
  end

  it "Comparacion de numeros con igual_a" do
    expect(7.deberia ser igual_a 7).to be(true)
  end

  it "Comparacion de numeros con distinto_a" do
    expect(7.deberia ser distinto_a -7).to be(true)
  end

  it "Comparacion de cadenas con igual_a" do
    expect("tadp".deberia ser igual_a "tadp")
  end

  it "Comparacion de cadena distinto_a" do
    expect("tadp".deberia ser distinto_a "dds").to be(true)
  end

  it "Comparacion trivial de booleanos" do
    expect(true.deberia ser igual_a true).to be(true)
  end

  it "Comparo con mensaje Ser" do
    expect(false.deberia ser false).to be(true)
  end

  it "Busco en una lista una coincidencia" do
    expect(7.deberia ser uno_de_estos [5, 7, "tadp"]).to be(true)
  end

  it "Busco en muchos parametros una coincidencia" do
    expect(7.deberia ser uno_de_estos 5, 7, "tadp").to be(true)
  end

  it "Mensaje Ser explota" do
    expect{
      false.deberia ser true
    }.to raise_error(TadspecAssertionError)
  end

  #Test con Clase Persona
  let(:persona_joven) { Persona.new('Pepe', 25) }
  let(:persona_vieja) { Persona.new('Pancracio', 35) }

  it 'Persona vieja deberia ser vieja' do
    expect(persona_vieja.viejo?.deberia ser true).to be(true)
  end

  it 'Persona Joven no deberia ser vieja' do
    expect(persona_joven.viejo?.deberia ser false).to be(true)
  end

  it 'Persona joven deberia llamarse Pepe y tiene 25 años' do
    expect(persona_joven.nombre.deberia ser 'Pepe').to be(true)
    expect(persona_joven.edad.deberia ser 25).to be(true)
  end

  it 'Persona vieja deberia llamarse Pancracio' do
    expect(persona_vieja.nombre.deberia ser 'Pancracio').to be(true)
    expect(persona_vieja.edad.deberia ser 35).to be(true)
  end
end



