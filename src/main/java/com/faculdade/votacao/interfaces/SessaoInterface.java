package com.faculdade.votacao.interfaces;

import java.time.LocalDateTime;
import java.util.Optional;

import com.faculdade.votacao.dto.SessaoResponseDTO;
import com.faculdade.votacao.model.Sessao;

public interface SessaoInterface {
    SessaoResponseDTO abrirSessao(Long pautaId, Integer minutos);
    SessaoResponseDTO agendarSessao(Long pautaId, LocalDateTime dataInicio, LocalDateTime dataFim, Integer duracaoMinutos);
    SessaoResponseDTO buscarSessaoPorId(Long id);
    Optional<Sessao> buscarPorId(Long id);
}