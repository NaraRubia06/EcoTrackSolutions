package br.unipar.programacaoweb.ecotracksolutions.exception;

//EstacaoNotFoundException = Exceção quando uma estação não é encontrada

public class EstacaoNotFoundException extends RuntimeException {
    public EstacaoNotFoundException(String message) {
        super(message);
    }
}