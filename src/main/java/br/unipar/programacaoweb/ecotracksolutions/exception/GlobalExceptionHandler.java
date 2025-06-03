package br.unipar.programacaoweb.ecotracksolutions.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EstacaoNotFoundException.class)
    public ResponseEntity<?> handleEstacaoNotFound(EstacaoNotFoundException ex) {
        return gerarRespostaErro(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LeituraInvalidaException.class)
    public ResponseEntity<?> handleLeituraInvalida(LeituraInvalidaException ex) {
        return gerarRespostaErro(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleOutrosErros(Exception ex) {
        // Logger.error("Erro interno", ex);
        return gerarRespostaErro("Erro interno: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<?> gerarRespostaErro(String mensagem, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("erro", mensagem);
        return new ResponseEntity<>(body, status);
    }
}
