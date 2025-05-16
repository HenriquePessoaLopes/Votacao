package com.faculdade.votacao.interfaces;

import com.faculdade.votacao.dto.ResultadoVotacaoDTO;
import com.faculdade.votacao.enums.OpcaoVoto;
import com.faculdade.votacao.model.Voto;

public interface VotoInterface {
    Voto registrarVoto(Long pautaId, Long associadoId, OpcaoVoto opcao);
    ResultadoVotacaoDTO contabilizarVotos(Long pautaId);
}
