package com.faculdade.votacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.faculdade.votacao.model.Pauta;

@Repository 
public interface PautaRepository extends JpaRepository<Pauta, Long> {
    
    
}
