package com.faculdade.votacao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para retornar dados de associado")
public class AssociadoResponseDTO {
    
    @Schema(description = "ID do associado", example = "1")
    private Long id;
    
    @Schema(description = "Nome do associado", example = "João da Silva")
    private String nome;
    
    @Schema(description = "CPF do associado", example = "123.456.789-01")
    private String cpf;
}