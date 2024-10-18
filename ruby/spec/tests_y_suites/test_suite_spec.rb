# frozen_string_literal: true

describe "Testear que se recolectan los métodos" do
  include Criteria

  let(:leandro) { Persona.new('leandro', 22) }

  it 'Se registra la Suite' do
    TADsPec.registrar_suite(MiSuite)
    expect(TADsPec.ver_suites).to eq([MiSuite])
  end

  it 'Se ejecutan algunos test especificados de MiSuite y falla 1' do
    TADsPec.registrar_suite(MiSuite)
    TADsPec.testear(MiSuite,
                    :testear_que_leandro_tiene_22,
                    :testear_que_7_es_mayor_a_5,
                    :testear_que_explota,
                    :testear_que_falla)
  end

  it 'Ejecuta todos los tests de MiSuite, fallan 2 y explotan 2' do
    TADsPec.registrar_suite(MiSuite)
    TADsPec.testear(MiSuite)
  end

  it 'Ejecuta todos los suites con sus respectivos tests' do
    TADsPec.registrar_suite(MiSuite)
    TADsPec.testear
  end
end