package com.faculdade.votacao.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.faculdade.votacao.dto.PautaRequestDTO;
import com.faculdade.votacao.dto.PautaResponseDTO;
import com.faculdade.votacao.dto.ErroResponseDTO;
import com.faculdade.votacao.exception.BusinessException;
import com.faculdade.votacao.interfaces.PautaInterface;

import java.util.List;

@Tag(name = "Pautas", description = "Endpoints para gerenciamento de pautas")
@RestController
@RequestMapping("/api/v1/pautas")
public class PautaController {

    private final PautaInterface pautaInterface;

    public PautaController(PautaInterface pautaInterface) {
        this.pautaInterface = pautaInterface;
    }

    @Operation(summary = "Cadastra uma nova pauta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pauta criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<?> cadastrarPauta(@RequestBody PautaRequestDTO request) {
        try {
            PautaResponseDTO response = pautaInterface.cadastrar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ErroResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        } catch (BusinessException e) {
            return ResponseEntity.status(e.getStatus())
                .body(new ErroResponseDTO(e.getMessage(), e.getStatus().value()));
        }
    }

    @Operation(summary = "Busca uma pauta pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pauta encontrada"),
        @ApiResponse(responseCode = "204", description = "Nenhuma pauta encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPautaPorId(@PathVariable Long id) {
        try {
            PautaResponseDTO response = pautaInterface.buscarPauta(id);
            if (response != null) {
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.noContent().build();
        } catch (BusinessException e) {
            return ResponseEntity.status(e.getStatus())
                .body(new ErroResponseDTO(e.getMessage(), e.getStatus().value()));
        }
    }

    @Operation(summary = "Lista todas as pautas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pautas retornada com sucesso"),
        @ApiResponse(responseCode = "204", description = "Nenhuma pauta encontrada")
    })
    @GetMapping
    public ResponseEntity<List<PautaResponseDTO>> listarTodasPautas() {
        List<PautaResponseDTO> pautas = pautaInterface.listarTodas();
        if (pautas == null || pautas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pautas);
    }
}