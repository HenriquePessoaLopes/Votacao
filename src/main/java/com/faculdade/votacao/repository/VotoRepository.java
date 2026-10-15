package com.faculdade.votacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.faculdade.votacao.model.Voto;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {

    boolean existsByAssociadoIdAndPautaId(Long associadoId, Long pautaId);

    List<Voto> findByPautaId(Long pautaId);
}
