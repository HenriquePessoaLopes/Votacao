package com.faculdade.votacao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.faculdade.votacao.enums.OpcaoVoto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Objeto para registro de voto")
public class VotoDTO {
    @Schema(description = "ID da pauta", example = "1")
    private Long pautaId;
    
    @Schema(description = "ID do associado", example = "1")
    private Long associadoId;
    
    @Schema(description = "Opção de voto", example = "SIM", allowableValues = {"SIM", "NAO"})
    private OpcaoVoto opcao;
}