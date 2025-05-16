package com.faculdade.votacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faculdade.votacao.interfaces.PautaInterface;
import com.faculdade.votacao.model.Pauta;
import com.faculdade.votacao.repository.PautaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PautaService implements PautaInterface {

    @Autowired
    private PautaRepository pautaRepository;
    
    public Pauta cadastrar(Pauta pauta) {
        return pautaRepository.save(pauta);
    }
    
    public Optional<Pauta> buscarPorId(Long id) {
        return pautaRepository.findById(id);
    }
    
    public List<Pauta> listarTodas() {
        return pautaRepository.findAll();
    }
}
