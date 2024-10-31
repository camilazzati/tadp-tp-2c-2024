require_relative 'criteria'

module Spy
  def self.espiar(objeto)
    # Crear un proxy del objeto que registre las llamadas a métodos
    spy = objeto.dup
    spy.define_singleton_method(:spy_registry) { @spy_registry ||= [] }
    objeto_original = objeto

    # Definir un método para verificar si un método fue llamado
    def spy.haber_recibido(metodo)
      llamadas = spy_registry.select { |registro| registro[:metodo] == metodo }
      if llamadas.empty?
        raise "El método #{metodo} no fue llamado."
      else
        VerificadorLlamada.new(llamadas)
      end
    end

    # Redefinir los métodos del objeto para interceptar las llamadas
    objeto.public_methods(false).each do |metodo|
      spy.define_singleton_method(metodo) do |*args, &block|
        spy_registry << { metodo: metodo, argumentos: args }
        # Llamar al método original del objeto usando send
        objeto_original.send(metodo, *args, &block)
      end
    end

    # Devolver el objeto espiado
    spy
  end

  class VerificadorLlamada
    def initialize(llamadas)
      @llamadas = llamadas
      @verificaciones = []
    end

    def veces(cantidad)
      @verificaciones << proc {
        if @llamadas.size != cantidad
          raise "Se esperaba que el método se llamara #{cantidad} veces, pero se llamó #{@llamadas.size} veces."
        end
      }
      self
    end

    def con_argumentos(*args)
      @verificaciones << proc {
        match = @llamadas.any? { |registro| registro[:argumentos] == args }
        raise "El método no fue llamado con los argumentos esperados." unless match
      }
      self
    end

    # Este método ejecuta todas las verificaciones acumuladas cuando se llama al mensaje Deberia
    def to_proc
      proc do
        @verificaciones.each(&:call)
        true
      end
    end
  end
end
