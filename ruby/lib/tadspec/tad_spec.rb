# frozen_string_literal: true

module Criteria
  class Config
    def initialize(proc)
      @proc = proc
    end

    def call(object)
      @proc.call(object)
    end
  end
end

class Object
  def deberia(criteria)
    criteria.call(self)
  end
end