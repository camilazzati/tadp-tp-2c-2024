# frozen_string_literal: true

describe "Testear que se recolectan los métodos" do
  include Criteria

  let(:leandro) { Persona.new('leandro', 22) }

  it 'Se registra la Suite' do
    TADsPec.registrar_suite(suitePrueba)
    expect(TADsPec.ver_suites).to eq([suitePrueba])
  end

  it 'Funciona con suite' do
    suitePrueba = Suite.new("SuitePrueba")
    suitePrueba.agregar_test("testear_que_7_es_mayor_a_5") do
      7.deberia ser mayor_a 5
    end
    suitePrueba.agregar_test("hacer_nada") do
      puts "Estoy haciendo nada..."
    end
    suitePrueba.agregar_test("testear_que_falla") do
      leandro = Persona.new('Leandro', 22)
      leandro.edad.deberia ser 23
    end
    suitePrueba.agregar_test("testear_que_leandro_tiene") do |edad|
      leandro = Persona.new('Leandro', 22)
      leandro.deberia tener_edad edad
    end
    TADsPec.registrar_suite(suitePrueba)
    TADsPec.testear(suitePrueba)
  end

  it 'Funciona con suite y metodos' do
    suitePrueba = Suite.new("SuitePrueba")
    suitePrueba.agregar_test("testear_que_7_es_mayor_a_5") do
      7.deberia ser mayor_a 5
    end
    suitePrueba.agregar_test("testear_que_falla") do
      leandro = Persona.new('Leandro', 22)
      leandro.edad.deberia ser 23
    end
    TADsPec.registrar_suite(suitePrueba)
    TADsPec.testear(suitePrueba, "7_es_mayor_a_5")
  end

  it 'Funciona sin parametros' do
    suitePrueba = Suite.new("SuitePrueba")
    suitePrueba.agregar_test("testear_que_7_es_mayor_a_5") do
      7.deberia ser mayor_a 5
    end
    suitePrueba.agregar_test("hacer_nada") do
      puts "Estoy haciendo nada..."
    end
    suitePrueba.agregar_test("testear_que_falla") do
      leandro = Persona.new('Leandro', 22)
      leandro.edad.deberia ser 23
    end
    suitePrueba.agregar_test("testear_que_leandro_tiene") do |edad|
      leandro = Persona.new('Leandro', 22)
      leandro.deberia tener_edad edad
    end
    TADsPec.registrar_suite(suitePrueba)
    TADsPec.testear
  end



end