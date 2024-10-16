describe "mensaje_entender" do
  include Criteria

  let(:leandro) { Persona.new('leandro', 22) }

  it 'leandro deberia entender viejo?' do
    expect(leandro.deberia entender :viejo?).to be(true)
  end

  it 'leandro deberia entender class' do
    expect(leandro.deberia entender :class).to be(true)
  end

  it 'leandro NO deberia entender joven?' do
    expect{leandro.deberia entender :joven?}.to raise_error(EntenderError)
  end
end