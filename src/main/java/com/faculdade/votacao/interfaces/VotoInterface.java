package com.faculdade.votacao.interfaces;

import com.faculdade.votacao.dto.ResultadoVotacaoDTO;
import com.faculdade.votacao.dto.VotoResponseDTO;
import com.faculdade.votacao.enums.OpcaoVoto;

public interface VotoInterface {
    VotoResponseDTO registrarVoto(Long pautaId, Long associadoId, OpcaoVoto opcao);
    ResultadoVotacaoDTO contabilizarVotos(Long pautaId);
}