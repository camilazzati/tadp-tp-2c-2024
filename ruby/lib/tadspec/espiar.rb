module Spy
  def self.espiar(objeto)
    # Crear un proxy del objeto original que registre las llamadas a métodos
    spy = objeto.dup
    spy.instance_variable_set(:@__spy_registry, [])

    # Guardar una referencia al objeto original
    original_objeto = objeto

    # Definir un método para obtener las interacciones registradas
    def spy.spy_registry
      @__spy_registry
    end

    # Definir un método para verificar si un método fue llamado
    def spy.haber_recibido(metodo)
      llamadas = @__spy_registry.select { |registro| registro[:metodo] == metodo }
      if llamadas.empty?
        raise "El método #{metodo} no fue llamado."
      else
        VerificadorLlamada.new(llamadas)
      end
    end

    # Redefinir los métodos del objeto para interceptar las llamadas
    objeto.public_methods(false).each do |metodo|
      spy.define_singleton_method(metodo) do |*args, &block|
        @__spy_registry << { metodo: metodo, argumentos: args }
        # Llamar al método original del objeto usando send
        original_objeto.send(metodo, *args, &block)
      end
    end

    # Devolver el objeto espiado
    spy
  end

  class VerificadorLlamada
    def initialize(llamadas)
      @llamadas = llamadas
    end

    def veces(cantidad)
      raise "Se esperaba que el método se llamara #{cantidad} veces, pero se llamó #{@llamadas.size} veces." if @llamadas.size != cantidad
      self
    end

    def con_argumentos(*args)
      match = @llamadas.any? { |registro| registro[:argumentos] == args }
      raise "El método no fue llamado con los argumentos esperados." unless match
      self
    end
  end
end
