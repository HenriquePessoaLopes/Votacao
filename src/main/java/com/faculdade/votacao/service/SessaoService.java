package com.faculdade.votacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faculdade.votacao.interfaces.SessaoInterface;
import com.faculdade.votacao.model.Pauta;
import com.faculdade.votacao.model.Sessao;
import com.faculdade.votacao.repository.PautaRepository;
import com.faculdade.votacao.repository.SessaoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SessaoService implements SessaoInterface {

    @Autowired
    private SessaoRepository sessaoRepository;
    
    @Autowired
    private PautaRepository pautaRepository;
    
    public Sessao abrirSessao(Long pautaId, Integer minutos) {

        if (minutos == null || minutos <= 0) {
            minutos = 1;
        }
        
        Pauta pauta = pautaRepository.findById(pautaId)
                .orElseThrow(() -> new IllegalArgumentException("Pauta não encontrada"));
        
        if (pauta.getSessao() != null) {
            throw new IllegalStateException("Esta pauta já possui uma sessão de votação");
        }
        
        Sessao sessao = new Sessao();
        sessao.setPauta(pauta);
        sessao.setDataAbertura(LocalDateTime.now());
        sessao.setDataFechamento(LocalDateTime.now().plusMinutes(minutos));
        
        return sessaoRepository.save(sessao);
    }
    
    public Optional<Sessao> buscarPorId(Long id) {
        return sessaoRepository.findById(id);
    }
}
