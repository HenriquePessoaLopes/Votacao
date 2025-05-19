package com.faculdade.votacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.faculdade.votacao.dto.PautaRequestDTO;
import com.faculdade.votacao.dto.PautaResponseDTO;
import com.faculdade.votacao.exception.BusinessException;
import com.faculdade.votacao.interfaces.PautaInterface;
import com.faculdade.votacao.mapper.PautaMapper;
import com.faculdade.votacao.model.Pauta;
import com.faculdade.votacao.repository.PautaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PautaService implements PautaInterface {

    @Autowired
    private PautaRepository pautaRepository;
    
    @Autowired
    private PautaMapper pautaMapper;
    
    @Override
    public PautaResponseDTO cadastrar(PautaRequestDTO pautaRequest) {
        validarPauta(pautaRequest);
        
        try {
            Pauta pauta = pautaMapper.toEntity(pautaRequest);
            Pauta pautaSalva = pautaRepository.save(pauta);
            return pautaMapper.toDto(pautaSalva);
        } catch (Exception e) {
            throw new BusinessException("Erro ao cadastrar pauta", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    private void validarPauta(PautaRequestDTO pautaRequest) {
        if (pautaRequest.getTitulo() == null || pautaRequest.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("Título da pauta é obrigatório");
        }
    }
    
    @Override
    public PautaResponseDTO buscarPauta(Long id) {
        Pauta pauta = pautaRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Pauta não encontrada", HttpStatus.NOT_FOUND));
        return pautaMapper.toDto(pauta);
    }
    
    @Override
    public Optional<Pauta> buscarPorId(Long id) {
        return pautaRepository.findById(id);
    }
    
    @Override
    public List<PautaResponseDTO> listarTodas() {
        List<Pauta> pautas = pautaRepository.findAll();
        return pautaMapper.toDtoList(pautas);
    }
}