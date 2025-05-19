package com.faculdade.votacao.mapper;

import com.faculdade.votacao.dto.VotoResponseDTO;
import com.faculdade.votacao.model.Voto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VotoMapper {
    
    public VotoResponseDTO toDto(Voto voto) {
        return VotoResponseDTO.builder()
                .id(voto.getId())
                .pautaId(voto.getPauta().getId())
                .tituloPauta(voto.getPauta().getTitulo())
                .associadoId(voto.getAssociado().getId())
                .nomeAssociado(voto.getAssociado().getNome())
                .opcao(voto.getVotoFavoravel())
                .dataVoto(voto.getDataVoto())
                .build();
    }
    
    public List<VotoResponseDTO> toDtoList(List<Voto> votos) {
        return votos.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}