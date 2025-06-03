package br.unipar.programacaoweb.ecotracksolutions.exception;

// LeituraInvalidaException = Exceção quando uma leitura é inválida (valor, unidade...)

public class LeituraInvalidaException extends RuntimeException {
    public LeituraInvalidaException(String message) {
        super(message);
    }
}