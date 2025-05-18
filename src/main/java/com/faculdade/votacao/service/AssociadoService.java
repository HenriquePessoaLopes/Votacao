package com.faculdade.votacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.faculdade.votacao.dto.AssociadoRequestDTO;
import com.faculdade.votacao.dto.AssociadoResponseDTO;
import com.faculdade.votacao.exception.BusinessException;
import com.faculdade.votacao.interfaces.AssociadoInterface;
import com.faculdade.votacao.mapper.AssociadoMapper;
import com.faculdade.votacao.model.Associado;
import com.faculdade.votacao.repository.AssociadoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AssociadoService implements AssociadoInterface {

    @Autowired
    private AssociadoRepository associadoRepository;
    
    @Autowired
    private AssociadoMapper associadoMapper;
    
    public AssociadoResponseDTO cadastrar(AssociadoRequestDTO dto) {
        validarAssociado(dto);
        
        try {
            Associado associado = associadoMapper.toEntity(dto);
            Associado salvo = associadoRepository.save(associado);
            return associadoMapper.toDto(salvo);
        } catch (Exception e) {
            throw new BusinessException("Não foi possível concluir o cadastro. Verifique os dados informados.", 
                                       HttpStatus.CONFLICT);
        }
    }
    
    private void validarAssociado(AssociadoRequestDTO dto) {
        if (dto.getCpf() == null || !dto.getCpf().matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF inválido. Deve conter 11 dígitos numéricos.");
        }
        
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
    }
    
    public AssociadoResponseDTO buscarAssociado(Long id) {
        Associado associado = associadoRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Associado não encontrado", HttpStatus.NOT_FOUND));
        return associadoMapper.toDto(associado);
    }
    
    public Optional<Associado> buscarPorId(Long id) {
        return associadoRepository.findById(id);
    }
    
    public List<AssociadoResponseDTO> listarTodos() {
        List<Associado> associados = associadoRepository.findAll();
        return associadoMapper.toDtoList(associados);
    }
}