package com.faculdade.votacao.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.faculdade.votacao.dto.AssociadoRequestDTO;
import com.faculdade.votacao.dto.AssociadoResponseDTO;
import com.faculdade.votacao.dto.ErroResponseDTO;
import com.faculdade.votacao.exception.BusinessException;
import com.faculdade.votacao.interfaces.AssociadoInterface;

import java.util.List;

@Tag(name = "Associados", description = "Endpoints para gerenciamento de associados")
@RestController
@RequestMapping("/api/v1/associados")
public class AssociadoController {

    private final AssociadoInterface associadoInterface;

    public AssociadoController(AssociadoInterface associadoInterface) {
        this.associadoInterface = associadoInterface;
    }
    
    @Operation(summary = "Cadastra um novo associado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Associado criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "CPF já cadastrado")
    })
    @PostMapping
    public ResponseEntity<?> cadastrarAssociado(@RequestBody AssociadoRequestDTO request) {
        try {
            AssociadoResponseDTO response = associadoInterface.cadastrar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ErroResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        } catch (BusinessException e) {
            return ResponseEntity.status(e.getStatus())
                .body(new ErroResponseDTO(e.getMessage(), e.getStatus().value()));
        }
    }
    
    @Operation(summary = "Busca um associado pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Associado encontrado"),
        @ApiResponse(responseCode = "404", description = "Associado não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            AssociadoResponseDTO response = associadoInterface.buscarAssociado(id);
            return ResponseEntity.ok(response);
        } catch (BusinessException e) {
            return ResponseEntity.status(e.getStatus())
                .body(new ErroResponseDTO(e.getMessage(), e.getStatus().value()));
        }
    }
    
    @Operation(summary = "Lista todos os associados")
    @ApiResponse(responseCode = "200", description = "Lista de associados retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<AssociadoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(associadoInterface.listarTodos());
    }
}