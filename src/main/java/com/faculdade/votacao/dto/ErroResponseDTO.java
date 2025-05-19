package com.faculdade.votacao.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErroResponseDTO {
    private LocalDateTime timestamp;
    private String mensagem;
    private Integer status;
    
    public ErroResponseDTO(String mensagem, Integer status) {
        this.timestamp = LocalDateTime.now();
        this.mensagem = mensagem;
        this.status = status;
    }
}