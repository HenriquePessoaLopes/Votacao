package com.faculdade.votacao.interfaces;

import java.util.List;
import java.util.Optional;

import com.faculdade.votacao.dto.AssociadoRequestDTO;
import com.faculdade.votacao.dto.AssociadoResponseDTO;
import com.faculdade.votacao.model.Associado;

public interface AssociadoInterface {
    AssociadoResponseDTO cadastrar(AssociadoRequestDTO associado);
    AssociadoResponseDTO buscarAssociado(Long id);
    Optional<Associado> buscarPorId(Long id);
    List<AssociadoResponseDTO> listarTodos();
}