# frozen_string_literal: true

describe "Testear que se recolectan los métodos" do
  include Criteria

  let(:leandro) { Persona.new('leandro', 22) }

  it 'Se registra la Suite' do
    TADsPec.registrar_suite(MiSuite)

    expect(TADsPec.ver_suites).to eq([MiSuite])
  end

  it 'Se ejecuta correctamente' do
    TADsPec.registrar_suite(MiSuite)
  end

  it 'Funciona con suite' do
    TADsPec.registrar_suite(MiSuite)
    TADsPec.testear(MiSuite)
  end

  it 'Funciona con suite y metodos' do
    TADsPec.registrar_suite(MiSuite)
    TADsPec.testear(MiSuite, "7_es_mayor_a_5")
  end

  it 'Explota' do
    TADsPec.registrar_suite(MiSuite)
    TADsPec.testear(MiSuite, "explota")
  end

  it 'Falla' do
    TADsPec.registrar_suite(MiSuite)
    TADsPec.testear(MiSuite, "falla")
  end

  it 'Funciona sin parametros' do
    TADsPec.registrar_suite(MiSuite)
    TADsPec.testear
  end



end