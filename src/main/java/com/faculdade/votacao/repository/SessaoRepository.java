package com.faculdade.votacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.faculdade.votacao.model.Sessao;

@Repository 
public interface SessaoRepository extends JpaRepository<Sessao, Long> {
    
    
}
