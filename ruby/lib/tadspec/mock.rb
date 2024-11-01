# frozen_string_literal: true
class Module
  def mockear(nombre_metodo, &bloque)
      clase = self
      # Esto falla porque guardar no es un metodo de clase
      MockRealizados.guardar(Mock.new(clase, nombre_metodo, &bloque))
  end
end

# Acá creo que les quedo a medio hacer si MockRealizado es un unico repositorio global o uno con varias instancias 
# porque initialize es algo que solo se llama cuando se crea una instancia pero restablecer es un metodo de clase

# queda MockRealizados como un unico repositorio global
module MockRealizados
  @mocks = []

  def self.guardar(mock)
    @mocks << mock
  end

  def self.restablecer
    @mocks.each { |mock| mock.restablecer}
    @mocks = []
  end

end

class Mock
  attr_accessor :clase, :metodo_original

  # No necesitas cambiarlo pero bloque original es un nombre bastante malo para identificar la definición del nuevo comportamiento
  def initialize(clase,nombre_metodo, &bloque_original)
    @clase = clase
    @metodo_original = clase.instance_method(nombre_metodo)
    clase.define_method(nombre_metodo, &bloque_original)
  end

  def restablecer
    # elimina el metodo mockeado de la clase que llamo a mockear
    #@clase.remove_method(@method_original.name)
    # si el mockeo lo hizo la clase dueña del metodo (osea no una clase que hereda de ella)
    #if @metodo_original.owner = @clase
      # lo restaura al original

    # directamente lo vuelve a definir, que lo pisa
    clase.define_method(@metodo_original.name, @metodo_original)
    #end

  end
end


