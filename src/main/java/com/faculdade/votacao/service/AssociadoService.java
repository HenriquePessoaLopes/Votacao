package com.faculdade.votacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faculdade.votacao.interfaces.AssociadoInterface;
import com.faculdade.votacao.model.Associado;
import com.faculdade.votacao.repository.AssociadoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AssociadoService implements AssociadoInterface {

    @Autowired
    private AssociadoRepository associadoRepository;
    
    public Associado cadastrar(Associado associado) {
        if (associado.getCpf() == null || !associado.getCpf().matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF inválido");
        }
        
        return associadoRepository.save(associado);
    }
    
    public Optional<Associado> buscarPorId(Long id) {
        return associadoRepository.findById(id);
    }
    
    public List<Associado> listarTodos() {
        return associadoRepository.findAll();
    }
}
