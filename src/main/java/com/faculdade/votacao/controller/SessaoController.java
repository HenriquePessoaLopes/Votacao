package com.faculdade.votacao.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.faculdade.votacao.dto.AberturaSessaoRequestDTO;
import com.faculdade.votacao.dto.SessaoResponseDTO;
import com.faculdade.votacao.dto.ErroResponseDTO;
import com.faculdade.votacao.exception.BusinessException;
import com.faculdade.votacao.interfaces.SessaoInterface;

@Tag(name = "Sessões", description = "Endpoints para gerenciamento de sessões de votação")
@RestController
@RequestMapping("/api/v1/sessoes")
public class SessaoController {

    private final SessaoInterface sessaoInterface;

    public SessaoController(SessaoInterface sessaoInterface) {
        this.sessaoInterface = sessaoInterface;
    }

   

    @Operation(summary = "Busca uma sessão pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessão encontrada"),
            @ApiResponse(responseCode = "404", description = "Sessão não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarSessao(@PathVariable Long id) {
        try {
            SessaoResponseDTO response = sessaoInterface.buscarSessaoPorId(id);
            return ResponseEntity.ok(response);
        } catch (BusinessException e) {
            return ResponseEntity.status(e.getStatus())
                    .body(new ErroResponseDTO(e.getMessage(), e.getStatus().value()));
        }
    }

    @Operation(summary = "Abre ou agenda uma nova sessão de votação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sessão aberta ou agendada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Sessão já existe para esta pauta")
    })
    @PostMapping
    public ResponseEntity<?> abrirSessao(@RequestBody AberturaSessaoRequestDTO request) {
        try {
            SessaoResponseDTO response;

            if (request.getDataInicio() != null || request.getDataFim() != null) {

                response = sessaoInterface.agendarSessao(
                        request.getPautaId(),
                        request.getDataInicio(),
                        request.getDataFim(),
                        request.getMinutos());
            } else {

                response = sessaoInterface.abrirSessao(request.getPautaId(), request.getMinutos());
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ErroResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErroResponseDTO(e.getMessage(), HttpStatus.CONFLICT.value()));
        } catch (BusinessException e) {
            return ResponseEntity.status(e.getStatus())
                    .body(new ErroResponseDTO(e.getMessage(), e.getStatus().value()));
        }
    }

}