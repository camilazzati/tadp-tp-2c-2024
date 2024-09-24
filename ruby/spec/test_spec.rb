describe "tadspec" do

  it "Objeto entiende Deberia" do
    expect(Object.new.respond_to? :deberia).to be(true)
  end

  it "Numero entiende Deberia" do
    expect(7.respond_to? :deberia).to be(true)
  end

  it "String entiende Deberia" do
    expect("Soy un String".respond_to? :deberia). to be(true)
  end
end