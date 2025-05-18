package com.faculdade.votacao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.faculdade.votacao.enums.OpcaoVoto;
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
@Schema(description = "DTO para retornar dados de voto")
public class VotoResponseDTO {
    
    @Schema(description = "ID do voto", example = "1")
    private Long id;
    
    @Schema(description = "ID da pauta", example = "1")
    private Long pautaId;
    
    @Schema(description = "Título da pauta", example = "Aumento de mensalidade")
    private String tituloPauta;
    
    @Schema(description = "ID do associado", example = "1")
    private Long associadoId;
    
    @Schema(description = "Nome do associado", example = "João Silva")
    private String nomeAssociado;
    
    @Schema(description = "Opção de voto", example = "SIM")
    private OpcaoVoto opcao;
    
    @Schema(description = "Data e hora do voto")
    private LocalDateTime dataVoto;
}