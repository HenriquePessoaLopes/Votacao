package com.faculdade.votacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.faculdade.votacao.model.Voto;

@Repository 
public interface VotoRepository extends JpaRepository<Voto, Long> {
    
    
}