package com.barbershop.api.domain.exceptions;

public class RegraNegocioException extends RuntimeException {
   public RegraNegocioException(String message) {
      super(message);
   }
}