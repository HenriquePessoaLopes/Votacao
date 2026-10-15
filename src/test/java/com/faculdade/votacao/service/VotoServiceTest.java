package com.faculdade.votacao.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.faculdade.votacao.dto.ResultadoVotacaoDTO;
import com.faculdade.votacao.enums.OpcaoVoto;
import com.faculdade.votacao.mapper.VotoMapper;
import com.faculdade.votacao.model.Associado;
import com.faculdade.votacao.model.Pauta;
import com.faculdade.votacao.model.Sessao;
import com.faculdade.votacao.model.Voto;
import com.faculdade.votacao.repository.AssociadoRepository;
import com.faculdade.votacao.repository.PautaRepository;
import com.faculdade.votacao.repository.VotoRepository;

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;
    @Mock
    private PautaRepository pautaRepository;
    @Mock
    private AssociadoRepository associadoRepository;
    @Mock
    private VotoMapper votoMapper;

    @InjectMocks
    private VotoService votoService;

    private Pauta pautaComSessao(LocalDateTime abertura, LocalDateTime fechamento) {
        Pauta pauta = new Pauta();
        pauta.setTitulo("Pauta de teste");
        Sessao sessao = new Sessao();
        sessao.setPauta(pauta);
        sessao.setDataAbertura(abertura);
        sessao.setDataFechamento(fechamento);
        pauta.setSessao(sessao);
        return pauta;
    }

    private Voto voto(OpcaoVoto opcao) {
        Voto voto = new Voto();
        voto.setVotoFavoravel(opcao);
        return voto;
    }

    @Test
    void naoAceitaVotoQuandoSessaoJaEncerrou() {
        Pauta pauta = pautaComSessao(LocalDateTime.now().minusMinutes(10), LocalDateTime.now().minusMinutes(5));
        when(associadoRepository.findById(1L)).thenReturn(Optional.of(new Associado()));
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));

        assertThrows(IllegalStateException.class, () -> votoService.registrarVoto(1L, 1L, OpcaoVoto.SIM));
        verify(votoRepository, never()).save(any());
    }

    @Test
    void naoAceitaVotoDuplicadoDoMesmoAssociado() {
        Pauta pauta = pautaComSessao(LocalDateTime.now().minusMinutes(1), LocalDateTime.now().plusMinutes(5));
        when(associadoRepository.findById(1L)).thenReturn(Optional.of(new Associado()));
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        when(votoRepository.existsByAssociadoIdAndPautaId(1L, 1L)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> votoService.registrarVoto(1L, 1L, OpcaoVoto.NAO));
        verify(votoRepository, never()).save(any());
    }

    @Test
    void naoContabilizaEnquantoSessaoEstaAberta() {
        Pauta pauta = pautaComSessao(LocalDateTime.now().minusMinutes(1), LocalDateTime.now().plusMinutes(5));
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));

        assertThrows(IllegalStateException.class, () -> votoService.contabilizarVotos(1L));
    }

    @Test
    void pautaEhAprovadaQuandoSimVenceDepoisDoEncerramento() {
        Pauta pauta = pautaComSessao(LocalDateTime.now().minusMinutes(10), LocalDateTime.now().minusMinutes(5));
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        when(votoRepository.findByPautaId(1L))
                .thenReturn(List.of(voto(OpcaoVoto.SIM), voto(OpcaoVoto.SIM), voto(OpcaoVoto.NAO)));

        ResultadoVotacaoDTO resultado = votoService.contabilizarVotos(1L);

        assertEquals(3, resultado.getTotalVotos());
        assertEquals(2, resultado.getVotosSim());
        assertEquals(1, resultado.getVotosNao());
        assertEquals("APROVADA", resultado.getResultado());
    }

    @Test
    void pautaTerminaEmEmpateQuandoVotosSaoIguais() {
        Pauta pauta = pautaComSessao(LocalDateTime.now().minusMinutes(10), LocalDateTime.now().minusMinutes(5));
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        when(votoRepository.findByPautaId(1L)).thenReturn(List.of(voto(OpcaoVoto.SIM), voto(OpcaoVoto.NAO)));

        assertEquals("EMPATE", votoService.contabilizarVotos(1L).getResultado());
    }
}
