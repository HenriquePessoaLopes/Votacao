
package com.faculdade.votacao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data              
@NoArgsConstructor  
@AllArgsConstructor 
@Builder           
@Schema(description = "DTO para retornar o resultado de uma votação")
public class ResultadoVotacaoDTO {
    
    @Schema(description = "ID da pauta", example = "1")
    private Long pautaId;
    
    @Schema(description = "Título da pauta", example = "Aumento de mensalidade")
    private String tituloPauta;
    
    @Schema(description = "Total de votos computados", example = "15")
    private Integer totalVotos;
    
    @Schema(description = "Quantidade de votos 'SIM'", example = "10")
    private Integer votosSim;
    
    @Schema(description = "Quantidade de votos 'NÃO'", example = "5")
    private Integer votosNao;
    
    @Schema(description = "Resultado da votação", example = "APROVADA", 
            allowableValues = {"APROVADA", "REPROVADA", "EMPATE", "SEM VOTOS", "SEM SESSÃO"})
    private String resultado;
}
