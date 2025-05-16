package com.faculdade.votacao.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.faculdade.votacao.model.Pauta;
import com.faculdade.votacao.service.PautaService;

@Tag(name = "Pautas", description = "Endpoints para gerenciamento de pautas")
@RestController
@RequestMapping("/api/v1/pautas")
public class PautaController {

    @Autowired
    private PautaService pautaService;

    @Operation(summary = "Cadastra uma nova pauta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pauta criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<Pauta> cadastrarPauta(@RequestBody Pauta pauta) {
        Pauta novaPauta = pautaService.cadastrar(pauta);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaPauta);
    }
    
    @Operation(summary = "Busca uma pauta pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pauta encontrada"),
        @ApiResponse(responseCode = "404", description = "Pauta não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pauta> buscarPautaPorId(@PathVariable Long id) {
        return pautaService.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Lista todas as pautas")
    @ApiResponse(responseCode = "200", description = "Lista de pautas retornada com sucesso")
    @GetMapping
    public ResponseEntity<Iterable<Pauta>> listarTodasPautas() {
        return ResponseEntity.ok(pautaService.listarTodas());
    }
}