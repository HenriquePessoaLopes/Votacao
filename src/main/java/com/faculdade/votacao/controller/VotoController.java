package com.faculdade.votacao.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.faculdade.votacao.dto.ResultadoVotacaoDTO;
import com.faculdade.votacao.dto.VotoRequestDTO;
import com.faculdade.votacao.dto.VotoResponseDTO;
import com.faculdade.votacao.dto.ErroResponseDTO;
import com.faculdade.votacao.exception.BusinessException;
import com.faculdade.votacao.interfaces.VotoInterface;

@Tag(name = "Votos", description = "Endpoints para gerenciamento de votos")
@RestController
@RequestMapping("/api/v1/votos")
public class VotoController {

    private final VotoInterface votoInterface;

    public VotoController(VotoInterface votoInterface) {
        this.votoInterface = votoInterface;
    }

    @Operation(summary = "Registra um novo voto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Voto registrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "Associado já votou nesta pauta")
    })
    @PostMapping
    public ResponseEntity<?> registrarVoto(@RequestBody VotoRequestDTO request) {
        try {
            VotoResponseDTO response = votoInterface.registrarVoto(
                request.getPautaId(),
                request.getAssociadoId(),
                request.getOpcao());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ErroResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        } catch (BusinessException e) {
            return ResponseEntity.status(e.getStatus())
                .body(new ErroResponseDTO(e.getMessage(), e.getStatus().value()));
        }
    }

    @Operation(summary = "Contabiliza os votos de uma pauta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Votos contabilizados com sucesso"),
        @ApiResponse(responseCode = "204", description = "Nenhum resultado encontrado"),
        @ApiResponse(responseCode = "400", description = "Sessão ainda está aberta")
    })
    @GetMapping("/resultado/{pautaId}")
    public ResponseEntity<?> contabilizarVotos(
        @Parameter(description = "ID da pauta para contabilizar votos", required = true) 
        @PathVariable Long pautaId) {
        try {
            ResultadoVotacaoDTO resultado = votoInterface.contabilizarVotos(pautaId);
            if (resultado != null) {
                return ResponseEntity.ok(resultado);
            }
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                .body(new ErroResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErroResponseDTO("Erro ao contabilizar votos: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
}