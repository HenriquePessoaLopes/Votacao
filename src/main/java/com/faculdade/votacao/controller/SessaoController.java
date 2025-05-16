package com.faculdade.votacao.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.faculdade.votacao.dto.AberturaSessaoDTO;
import com.faculdade.votacao.model.Sessao;
import com.faculdade.votacao.service.SessaoService;

@Tag(name = "Sessões", description = "Endpoints para gerenciamento de sessões de votação")
@RestController
@RequestMapping("/api/v1/sessoes")
public class SessaoController {

    @Autowired
    private SessaoService sessaoService;
    
    @Operation(summary = "Abre uma nova sessão de votação")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sessão aberta com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "Sessão já existe para esta pauta")
    })
    @PostMapping
    public ResponseEntity<Sessao> abrirSessao(@RequestBody AberturaSessaoDTO dto) {
        try {
            Sessao sessao = sessaoService.abrirSessao(dto.getPautaId(), dto.getMinutos());
            return ResponseEntity.status(HttpStatus.CREATED).body(sessao);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    
    @Operation(summary = "Busca uma sessão pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sessão encontrada"),
        @ApiResponse(responseCode = "404", description = "Sessão não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Sessao> buscarSessao(@PathVariable Long id) {
        return sessaoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}