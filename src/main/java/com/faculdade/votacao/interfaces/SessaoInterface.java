package com.faculdade.votacao.interfaces;

import java.util.Optional;

import com.faculdade.votacao.model.Sessao;

public interface SessaoInterface {
    Sessao abrirSessao(Long pautaId, Integer minutos);
    Optional<Sessao> buscarPorId(Long id);
}