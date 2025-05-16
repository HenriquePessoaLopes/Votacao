package com.faculdade.votacao.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultadoVotacaoDTO {
    private Long pautaId;
    private String tituloPauta;
    private int totalVotos;
    private int votosSim;
    private int votosNao;
    private String resultado; 
}