package com.faculdade.votacao.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.faculdade.votacao.enums.CpfStatus;
import com.faculdade.votacao.interfaces.CpfValidatorInterface;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cpf")
public class CpfValidatorController {

    private final CpfValidatorInterface cpfValidatorInterface;

    public CpfValidatorController(CpfValidatorInterface cpfValidatorInterface) {
        this.cpfValidatorInterface = cpfValidatorInterface;
    }
    
    @GetMapping("/validar/{cpf}")
    public ResponseEntity<Map<String, String>> validarCpf(@PathVariable String cpf) {
        try {
            CpfStatus status = cpfValidatorInterface.validarCpf(cpf);
            
            Map<String, String> response = new HashMap<>();
            response.put("status", status.name());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}