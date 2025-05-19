package com.faculdade.votacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.faculdade.votacao.dto.SessaoResponseDTO;
import com.faculdade.votacao.exception.BusinessException;
import com.faculdade.votacao.interfaces.SessaoInterface;
import com.faculdade.votacao.mapper.SessaoMapper;
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

    @Autowired
    private SessaoMapper sessaoMapper;

    @Override
    public SessaoResponseDTO abrirSessao(Long pautaId, Integer minutos) {
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

        Sessao sessaoSalva = sessaoRepository.save(sessao);
        return sessaoMapper.toDto(sessaoSalva);
    }

    @Override
    public SessaoResponseDTO buscarSessaoPorId(Long id) {
        Sessao sessao = sessaoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Sessão não encontrada", HttpStatus.NOT_FOUND));
        return sessaoMapper.toDto(sessao);
    }

    @Override
    public Optional<Sessao> buscarPorId(Long id) {
        return sessaoRepository.findById(id);
    }

    @Override
    public SessaoResponseDTO agendarSessao(Long pautaId, LocalDateTime dataInicio, LocalDateTime dataFim,
            Integer duracaoMinutos) {
        Pauta pauta = pautaRepository.findById(pautaId)
                .orElseThrow(() -> new IllegalArgumentException("Pauta não encontrada"));

        if (pauta.getSessao() != null) {
            throw new IllegalStateException("Esta pauta já possui uma sessão de votação");
        }

        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime inicio = dataInicio;
        LocalDateTime fim = dataFim;

        if (inicio == null) {
            inicio = agora;
        } else if (inicio.isBefore(agora)) {
            throw new IllegalArgumentException("A data de início deve ser futura");
        }

        if (fim == null) {
            if (duracaoMinutos == null || duracaoMinutos <= 0) {
                duracaoMinutos = 1;
            }
            fim = inicio.plusMinutes(duracaoMinutos);
        } else if (fim.isBefore(inicio) || fim.isEqual(inicio)) {
            throw new IllegalArgumentException("A data de término deve ser posterior à data de início");
        }

        Sessao sessao = new Sessao();
        sessao.setPauta(pauta);
        sessao.setDataAbertura(inicio);
        sessao.setDataFechamento(fim);

        Sessao sessaoSalva = sessaoRepository.save(sessao);
        return sessaoMapper.toDto(sessaoSalva);
    }
}