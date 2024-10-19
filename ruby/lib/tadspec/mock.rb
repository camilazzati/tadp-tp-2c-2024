# frozen_string_literal: true
class Module
  def mockear(nombre_metodo, &bloque)
      clase = self
      MockRealizados.guardar(Mock.new(clase, nombre_metodo, &bloque))
  end
end

module MockRealizados
  @mocks
  def initialize()
    @mocks = []
  end

  def guardar(mock)
    @mocks << mock
  end

  def self.restablecer
    @mocks.each { |mock| mock.restablecer}
    @mocks = []
  end

end

class Mock
  attr_accessor :clase, :metodo_original

  def initialize(clase,nombre_metodo, &bloque_original)
    @clase = clase
    @metodo_original = clase.instance_method(nombre_metodo)
    clase.define_method(nombre_metodo, &bloque_original)
  end

  def restablecer
    # elimina el metodo mockeado de la clase que llamo a mockear
    @clase.remove_method(@method_original.name)
    # si el mockeo lo hizo la clase dueña del metodo (osea no una clase que hereda de ella)
    if @metodo_original.owner = @clase
      # lo restaura al original
      clase.define_method(@metodo_original.name, @metodo_original)
    end

  end
end


