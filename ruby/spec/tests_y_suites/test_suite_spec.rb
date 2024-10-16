# frozen_string_literal: true

describe 'Testear que se recolectan los métodos' do
  include Criteria

  let(:leandro) { Persona.new('leandro', 22) }

  it 'Se registra la Suite' do
    TADsPec.registrar_suite(MiSuite)

    expect(TADsPec.ver_suites).to eq([MiSuite])
  end

  it 'Se ejecuta correctamente' do
    TADsPec.registrar_suite(MiSuite)
  end
end