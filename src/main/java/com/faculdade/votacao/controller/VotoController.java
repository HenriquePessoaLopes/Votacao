package com.faculdade.votacao.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.faculdade.votacao.dto.ResultadoVotacaoDTO;
import com.faculdade.votacao.dto.VotoDTO;
import com.faculdade.votacao.model.Voto;
import com.faculdade.votacao.service.VotoService;

@Tag(name = "Votos", description = "Endpoints para gerenciamento de votos")
@RestController
@RequestMapping("/api/v1/votos")
public class VotoController {

    @Autowired
    private VotoService votoService;
    
    @Operation(summary = "Registra um novo voto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Voto registrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "Associado já votou nesta pauta")
    })
    @PostMapping
    public ResponseEntity<Voto> registrarVoto(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do voto")
            @RequestBody VotoDTO voto) {
        try {
            Voto novoVoto = votoService.registrarVoto(
                voto.getPautaId(), 
                voto.getAssociadoId(), 
                voto.getOpcao()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(novoVoto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    
    @Operation(summary = "Contabiliza os votos de uma pauta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Votos contabilizados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pauta não encontrada"),
        @ApiResponse(responseCode = "400", description = "Sessão ainda está aberta")
    })
    @GetMapping("/resultado/{pautaId}")
    public ResponseEntity<ResultadoVotacaoDTO> contabilizarVotos(
            @Parameter(description = "ID da pauta para contabilizar votos", required = true)
            @PathVariable Long pautaId) {
        try {
            ResultadoVotacaoDTO resultado = votoService.contabilizarVotos(pautaId);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}