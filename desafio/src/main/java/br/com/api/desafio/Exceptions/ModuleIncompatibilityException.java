package br.com.api.desafio.Exceptions;

public class ModuleIncompatibilityException extends RuntimeException{
    public ModuleIncompatibilityException(String message){
        super(message);
    }
}
