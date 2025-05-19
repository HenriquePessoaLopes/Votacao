package com.faculdade.votacao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para retornar dados de sessão de votação")
public class SessaoResponseDTO {
    
    @Schema(description = "ID da sessão", example = "1")
    private Long id;
    
    @Schema(description = "ID da pauta associada", example = "1")
    private Long pautaId;
    
    @Schema(description = "Título da pauta", example = "Aumento de mensalidade")
    private String tituloPauta;
    
    @Schema(description = "Data e hora de abertura da sessão")
    private LocalDateTime dataAbertura;
    
    @Schema(description = "Data e hora de fechamento da sessão")
    private LocalDateTime dataFechamento;
    
    @Schema(description = "Status da sessão", example = "ABERTA")
    private String status;
}