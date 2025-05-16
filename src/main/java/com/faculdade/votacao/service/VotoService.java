package com.faculdade.votacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faculdade.votacao.dto.ResultadoVotacaoDTO;
import com.faculdade.votacao.enums.OpcaoVoto;
import com.faculdade.votacao.interfaces.VotoInterface;
import com.faculdade.votacao.model.Associado;
import com.faculdade.votacao.model.Pauta;
import com.faculdade.votacao.model.Voto;
import com.faculdade.votacao.repository.AssociadoRepository;
import com.faculdade.votacao.repository.PautaRepository;
import com.faculdade.votacao.repository.VotoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VotoService implements VotoInterface {

    @Autowired
    private VotoRepository votoRepository;
    
    @Autowired
    private PautaRepository pautaRepository;
    
    @Autowired
    private AssociadoRepository associadoRepository;
    
    public Voto registrarVoto(Long pautaId, Long associadoId, OpcaoVoto opcao) {
        Associado associado = associadoRepository.findById(associadoId)
                .orElseThrow(() -> new IllegalArgumentException("Associado não encontrado"));
        
        Pauta pauta = pautaRepository.findById(pautaId)
                .orElseThrow(() -> new IllegalArgumentException("Pauta não encontrada"));
        
        if (pauta.getSessao() == null || !pauta.getSessao().isAberta()) {
            throw new IllegalStateException("Esta pauta não possui uma sessão aberta para votação");
        }
        
        if (votoRepository.findAll().stream()
                .anyMatch(v -> v.getAssociado().getId().equals(associadoId) && 
                         v.getPauta().getId().equals(pautaId))) {
            throw new IllegalStateException("O associado já votou nesta pauta");
        }

        Voto voto = new Voto();
        voto.setAssociado(associado);
        voto.setPauta(pauta);
        voto.setVotoFavoravel(opcao);
        voto.setDataVoto(LocalDateTime.now());
        
        return votoRepository.save(voto);
    }
    
    public ResultadoVotacaoDTO contabilizarVotos(Long pautaId) {
        Pauta pauta = pautaRepository.findById(pautaId)
                .orElseThrow(() -> new IllegalArgumentException("Pauta não encontrada"));
        
        if (pauta.getSessao() != null && pauta.getSessao().isAberta()) {
            throw new IllegalStateException("A sessão de votação ainda está aberta");
        }
        
        List<Voto> votos = votoRepository.findAll().stream()
                .filter(v -> v.getPauta().getId().equals(pautaId))
                .toList();

        int votosSim = (int) votos.stream()
                .filter(v -> v.getVotoFavoravel() == OpcaoVoto.SIM)
                .count();
        
        int votosNao = (int) votos.stream()
                .filter(v -> v.getVotoFavoravel() == OpcaoVoto.NAO)
                .count();
        
        ResultadoVotacaoDTO resultado = new ResultadoVotacaoDTO();
        resultado.setPautaId(pautaId);
        resultado.setTituloPauta(pauta.getTitulo());
        resultado.setTotalVotos(votos.size());
        resultado.setVotosSim(votosSim);
        resultado.setVotosNao(votosNao);
        resultado.setResultado(votosSim > votosNao ? "APROVADA" : "REPROVADA");
        
        return resultado;
    }
}
