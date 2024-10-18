# frozen_string_literal: true

describe "Testear que se recolectan los métodos" do
  include Criteria

  let(:leandro) { Persona.new('leandro', 22) }

  it 'Se registra la Suite' do
    TADsPec.registrar_suite(MiSuite)
    expect(TADsPec.ver_suites).to eq([MiSuite])
  end

  it 'Funciona y Corre algunas test especificado de MiSuite' do
    TADsPec.registrar_suite(MiSuite)
    TADsPec.testear(MiSuite,
                    :testear_que_leandro_tiene_22,
                    :testear_que_7_es_mayor_a_5,
                    :testear_que_explota,
                    :testear_que_falla)
  end

  it 'Funciona y Corre todos los tests de MiSuite' do
    TADsPec.registrar_suite(MiSuite)
    TADsPec.testear(MiSuite)
  end

  it 'Funciona y Corre todos los suites' do
    TADsPec.registrar_suite(MiSuite)
    TADsPec.testear
  end

end