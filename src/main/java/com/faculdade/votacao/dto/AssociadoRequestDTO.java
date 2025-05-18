package com.faculdade.votacao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para receber dados de criação de associado")
public class AssociadoRequestDTO {
    
    @Schema(description = "Nome do associado", example = "João da Silva", required = true)
    private String nome;
    
    @Schema(description = "CPF do associado (11 dígitos numéricos)", example = "12345678901", required = true)
    private String cpf;
}