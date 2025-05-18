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
@Schema(description = "DTO para receber dados de criação de pauta")
public class PautaRequestDTO {
    
    @Schema(description = "Título da pauta", example = "Aumento de mensalidade", required = true)
    private String titulo;
    
    @Schema(description = "Descrição detalhada da pauta", example = "Proposta de aumento de 10% na mensalidade")
    private String descricao;
}