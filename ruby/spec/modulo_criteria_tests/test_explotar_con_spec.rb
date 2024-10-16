describe "mensaje explotar_con" do
  include Criteria

  let(:leandro) { Persona.new('leandro', 22) }

  it 'deberia explotar con ZeroDivisionError' do
    expect(en { 7 / 0 }.deberia explotar_con ZeroDivisionError).to be(true)
  end

  it 'deberia explotar con NoMethodError' do
    expect(en { leandro.joven? }.deberia explotar_con NoMethodError).to be(true)
  end

  # este test, segun esta en el enunciado tendria que explotar con Error, pero cuando pongo
  # Error en vez de StandardError, el test no pasa pero porque no reconoce Error, no entiendo por que
  it 'deberia explotar con Error' do
    expect(en { leandro.joven? }.deberia explotar_con StandardError).to be(true)
  end

  it 'No deberia explotar' do
    expect{en { leandro.viejo? }.deberia explotar_con NoMethodError}.to raise_error(WrongError)
  end

  it 'deberia explotar con otro error' do
    expect{en { 7 / 0 }.deberia explotar_con NoMethodError}.to raise_error(WrongError)
  end


  
end