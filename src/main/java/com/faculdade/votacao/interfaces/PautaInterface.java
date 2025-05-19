package com.faculdade.votacao.interfaces;

import java.util.List;
import java.util.Optional;

import com.faculdade.votacao.dto.PautaRequestDTO;
import com.faculdade.votacao.dto.PautaResponseDTO;
import com.faculdade.votacao.model.Pauta;

public interface PautaInterface {
    PautaResponseDTO cadastrar(PautaRequestDTO pautaRequest);
    PautaResponseDTO buscarPauta(Long id);
    Optional<Pauta> buscarPorId(Long id);
    List<PautaResponseDTO> listarTodas();
}