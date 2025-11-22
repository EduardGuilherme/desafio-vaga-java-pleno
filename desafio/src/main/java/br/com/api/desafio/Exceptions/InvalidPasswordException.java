package br.com.api.desafio.Exceptions;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException(String message){
        super(message);
    }
}
