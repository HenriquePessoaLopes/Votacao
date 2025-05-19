package com.faculdade.votacao.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO para abertura ou agendamento de sessão de votação")
public class AberturaSessaoRequestDTO {
    
    @NotNull(message = "O ID da pauta é obrigatório")
    @Schema(description = "ID da pauta", example = "1", required = true)
    private Long pautaId;
    
    @Schema(description = "Duração da sessão em minutos (default: 1)", example = "5")
    private Integer minutos;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Data e hora para início da sessão (formato ISO)", example = "2025-05-20T14:30:00")
    private LocalDateTime dataInicio;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Data e hora para término da sessão (formato ISO)", example = "2025-05-20T15:30:00")
    private LocalDateTime dataFim;
}