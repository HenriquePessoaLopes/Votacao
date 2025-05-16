package com.faculdade.votacao.interfaces;

import java.util.List;
import java.util.Optional;

import com.faculdade.votacao.model.Associado;

public interface AssociadoInterface {
    Associado cadastrar(Associado associado);
    Optional<Associado> buscarPorId(Long id);
    List<Associado> listarTodos();
}
