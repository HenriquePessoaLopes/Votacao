package com.faculdade.votacao.interfaces;

import java.util.List;
import java.util.Optional;

import com.faculdade.votacao.model.Pauta;

public interface PautaInterface {
    Pauta cadastrar(Pauta pauta);
    Optional<Pauta> buscarPorId(Long id);
    List<Pauta> listarTodas();
}
