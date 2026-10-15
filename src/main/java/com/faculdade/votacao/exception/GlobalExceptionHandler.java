package com.faculdade.votacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.faculdade.votacao.dto.ErroResponseDTO;

/**
 * Traduz regras de negocio violadas (ex.: voto duplicado, sessao ja existente)
 * em respostas HTTP 409 em vez de erro 500.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErroResponseDTO> tratarConflito(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErroResponseDTO(e.getMessage(), HttpStatus.CONFLICT.value()));
    }
}
