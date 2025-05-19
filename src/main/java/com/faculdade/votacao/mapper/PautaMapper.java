package com.faculdade.votacao.mapper;

import com.faculdade.votacao.dto.PautaRequestDTO;
import com.faculdade.votacao.dto.PautaResponseDTO;
import com.faculdade.votacao.model.Pauta;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PautaMapper {

    public Pauta toEntity(PautaRequestDTO dto) {
        Pauta pauta = new Pauta();
        pauta.setTitulo(dto.getTitulo());
        pauta.setDescricao(dto.getDescricao());
        return pauta;
    }
    
    public PautaResponseDTO toDto(Pauta pauta) {
        return PautaResponseDTO.builder()
                .id(pauta.getId())
                .titulo(pauta.getTitulo())
                .descricao(pauta.getDescricao())
                .build();
    }
    
    public List<PautaResponseDTO> toDtoList(List<Pauta> pautas) {
        return pautas.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}