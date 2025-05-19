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
@Schema(description = "DTO para retornar dados de pauta")
public class PautaResponseDTO {
    
    @Schema(description = "ID da pauta", example = "1")
    private Long id;
    
    @Schema(description = "Título da pauta", example = "Aumento de mensalidade")
    private String titulo;
    
    @Schema(description = "Descrição detalhada da pauta", example = "Proposta de aumento de 10% na mensalidade")
    private String descricao;
}