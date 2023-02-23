package io.github.curso.msavaliadorcredito.application.exception;

public class DadosClientesNotFoundException extends Exception{
    public DadosClientesNotFoundException() {
        super("Cliente não encontrado");
    }
}
