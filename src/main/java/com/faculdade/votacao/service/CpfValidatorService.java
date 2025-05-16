package com.faculdade.votacao.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.faculdade.votacao.enums.CpfStatus;
import com.faculdade.votacao.interfaces.CpfValidatorInterface;

import java.util.Random;

@Service
public class CpfValidatorService implements CpfValidatorInterface {
    
    private Random random = new Random();
    
    public CpfStatus validarCpf(String cpf) {

        if (cpf == null || !cpf.matches("\\d{11}")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CPF inválido");
        }

        if (random.nextDouble() <= 0.7) {

            if (random.nextDouble() <= 0.8) {
                return CpfStatus.ABLE_TO_VOTE;
            } else {
                return CpfStatus.UNABLE_TO_VOTE;
            }
        }
        
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CPF inválido");
    }
}