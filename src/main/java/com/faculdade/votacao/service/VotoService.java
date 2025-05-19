package com.faculdade.votacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faculdade.votacao.dto.ResultadoVotacaoDTO;
import com.faculdade.votacao.dto.VotoResponseDTO;
import com.faculdade.votacao.enums.OpcaoVoto;
import com.faculdade.votacao.interfaces.VotoInterface;
import com.faculdade.votacao.mapper.VotoMapper;
import com.faculdade.votacao.model.Associado;
import com.faculdade.votacao.model.Pauta;
import com.faculdade.votacao.model.Voto;
import com.faculdade.votacao.repository.AssociadoRepository;
import com.faculdade.votacao.repository.PautaRepository;
import com.faculdade.votacao.repository.VotoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VotoService implements VotoInterface {

        @Autowired
        private VotoRepository votoRepository;

        @Autowired
        private PautaRepository pautaRepository;

        @Autowired
        private AssociadoRepository associadoRepository;

        @Autowired
        private VotoMapper votoMapper;

        @Override
        public VotoResponseDTO registrarVoto(Long pautaId, Long associadoId, OpcaoVoto opcao) {
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

                Voto votoSalvo = votoRepository.save(voto);
                return votoMapper.toDto(votoSalvo);
        }

        @Override
        public ResultadoVotacaoDTO contabilizarVotos(Long pautaId) {
                Pauta pauta = pautaRepository.findById(pautaId)
                                .orElseThrow(() -> new IllegalArgumentException("Pauta não encontrada"));

                if (pauta.getSessao() == null) {

                        return ResultadoVotacaoDTO.builder()
                                        .pautaId(pautaId)
                                        .tituloPauta(pauta.getTitulo())
                                        .totalVotos(0)
                                        .votosSim(0)
                                        .votosNao(0)
                                        .resultado("SEM SESSÃO")
                                        .build();
                }

                if (pauta.getSessao().isAberta()) {
                        throw new IllegalStateException("A sessão de votação ainda está aberta");
                }

                List<Voto> votos = votoRepository.findAll().stream()
                                .filter(v -> v.getPauta().getId().equals(pautaId))
                                .collect(Collectors.toList());

                int votosSim = (int) votos.stream()
                                .filter(v -> v.getVotoFavoravel() == OpcaoVoto.SIM)
                                .count();

                int votosNao = (int) votos.stream()
                                .filter(v -> v.getVotoFavoravel() == OpcaoVoto.NAO)
                                .count();

                String resultado;
                if (votos.isEmpty()) {
                        resultado = "SEM VOTOS";
                } else if (votosSim > votosNao) {
                        resultado = "APROVADA";
                } else if (votosNao > votosSim) {
                        resultado = "REPROVADA";
                } else {
                        resultado = "EMPATE";
                }

                return ResultadoVotacaoDTO.builder()
                                .pautaId(pautaId)
                                .tituloPauta(pauta.getTitulo())
                                .totalVotos(votos.size())
                                .votosSim(votosSim)
                                .votosNao(votosNao)
                                .resultado(resultado)
                                .build();
        }

}