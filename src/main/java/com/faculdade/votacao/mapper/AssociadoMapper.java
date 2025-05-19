package com.faculdade.votacao.mapper;

import com.faculdade.votacao.dto.AssociadoRequestDTO;
import com.faculdade.votacao.dto.AssociadoResponseDTO;
import com.faculdade.votacao.model.Associado;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AssociadoMapper {

    public Associado toEntity(AssociadoRequestDTO dto) {
        Associado associado = new Associado();
        associado.setNome(dto.getNome());
        associado.setCpf(dto.getCpf());
        return associado;
    }
    
    public AssociadoResponseDTO toDto(Associado associado) {
        return AssociadoResponseDTO.builder()
                .id(associado.getId())
                .nome(associado.getNome())
                .cpf(associado.getCpf())
                .build();
    }
    
    public List<AssociadoResponseDTO> toDtoList(List<Associado> associados) {
        return associados.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}