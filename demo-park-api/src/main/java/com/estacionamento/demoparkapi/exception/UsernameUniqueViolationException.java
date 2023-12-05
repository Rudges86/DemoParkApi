package com.estacionamento.demoparkapi.exception;
//Ao invés de extender Throwable, que é a mais geral, extendeu a de execução
public class UsernameUniqueViolationException extends RuntimeException {
    public UsernameUniqueViolationException(String message) {
        super(message);
    }
}
