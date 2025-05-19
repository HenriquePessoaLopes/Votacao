package com.faculdade.votacao.mapper;

import com.faculdade.votacao.dto.SessaoResponseDTO;
import com.faculdade.votacao.model.Sessao;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SessaoMapper {
    
    public SessaoResponseDTO toDto(Sessao sessao) {
        boolean aberta = sessao.isAberta();
        String status = aberta ? "ABERTA" : (LocalDateTime.now().isBefore(sessao.getDataAbertura()) ? "PROGRAMADA" : "ENCERRADA");
        
        return SessaoResponseDTO.builder()
                .id(sessao.getId())
                .pautaId(sessao.getPauta().getId())
                .tituloPauta(sessao.getPauta().getTitulo())
                .dataAbertura(sessao.getDataAbertura())
                .dataFechamento(sessao.getDataFechamento())
                .status(status)
                .build();
    }
    
    public List<SessaoResponseDTO> toDtoList(List<Sessao> sessoes) {
        return sessoes.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}