package com.faculdade.votacao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.faculdade.votacao.enums.CpfStatus;
import com.faculdade.votacao.service.CpfValidatorService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cpf")
public class CpfValidatorController {

    @Autowired
    private CpfValidatorService cpfValidatorService;
    
    @GetMapping("/validar/{cpf}")
    public ResponseEntity<Map<String, String>> validarCpf(@PathVariable String cpf) {
        try {
            CpfStatus status = cpfValidatorService.validarCpf(cpf);
            
            Map<String, String> response = new HashMap<>();
            response.put("status", status.name());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}