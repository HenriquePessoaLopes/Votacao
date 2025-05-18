package com.faculdade.votacao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.faculdade.votacao.enums.OpcaoVoto;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para receber dados de voto")
public class VotoRequestDTO {
    
    @Schema(description = "ID da pauta", example = "1", required = true)
    private Long pautaId;
    
    @Schema(description = "ID do associado", example = "1", required = true)
    private Long associadoId;
    
    @Schema(description = "Opção de voto", example = "SIM", allowableValues = {"SIM", "NAO"}, required = true)
    private OpcaoVoto opcao;
}