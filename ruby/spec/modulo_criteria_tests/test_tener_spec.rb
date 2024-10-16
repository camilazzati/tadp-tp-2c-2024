# frozen_string_literal: true

=begin

Otra posible configuración para el deberia nos tiene que permitir verificar fácilmente los atributos de un objeto.
Para eso queremos poder disponer de una serie de mensajes tener_<<nombre de atributo>> que controle el valor de
dicho atributo para el objeto (ojo, no importa si el atributo tiene o no accesor, queremos chequear directo el estado interno).

leandro.deberia tener_edad 22 # pasa
leandro.deberia tener_nombre "leandro" # falla: no hay atributo nombre
leandro.deberia tener_nombre nil # pasa
leandro.deberia tener_edad mayor_a 20 # pasa
leandro.deberia tener_edad menor_a 25 # pasa
leandro.deberia tener_edad uno_de_estos [7, 22, "hola"] # pasa


------> objeto.deberia tener_<<nombre del atributo>> valor_esperado
=end

describe "Mensaje: Deberia <tener>" do
  include Criteria

  before(:each) do
    @leandro = Persona.new("Leandro", 22)
  end

  it 'Leandro tiene definida una edad' do
    expect(@leandro.deberia tener_edad 22).to be(true)
  end

  it "Mensaje Tener Explota" do
    expect{
      @leandro.deberia tener_apellido "leandrinho"
    }.to raise_error(NoVariableError)
  end

  it 'Leandro tiene definida una edad igual a 22' do
    expect(@leandro.deberia tener_edad igual_a 22).to be(true)
  end

  it 'Leandro tiene definida una edad mayor a 20' do
    expect(@leandro.deberia tener_edad mayor_a 20).to be(true)
  end

  it 'Leandro tiene definida una edad menor a 25' do
    expect(@leandro.deberia tener_edad menor_a 25).to be(true)
  end

  it 'Leandro tiene definida una edad entre ciertos valores' do
    expect(@leandro.deberia tener_edad uno_de_estos [7, 22, "hola"]).to be(true)
  end
end
=begin
it	it 'Leandro tiene una edad' do
		expect(@leandro.deberia tener_edad mayor_a 20).to be(true)
	end
=end
