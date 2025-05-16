package com.faculdade.votacao.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.faculdade.votacao.model.Associado;
import com.faculdade.votacao.service.AssociadoService;
import java.util.List;

@Tag(name = "Associados", description = "Endpoints para gerenciamento de associados")
@RestController
@RequestMapping("/api/v1/associados")
public class AssociadoController {

    @Autowired
    private AssociadoService associadoService;
    
    @Operation(summary = "Cadastra um novo associado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Associado criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "CPF já cadastrado")
    })
    @PostMapping
    public ResponseEntity<?> cadastrarAssociado(@RequestBody Associado associado) {
        try {
            Associado novoAssociado = associadoService.cadastrar(associado);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoAssociado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Não foi possível cadastrar o associado. O CPF já pode estar em uso.");
        }
    }
    
    @Operation(summary = "Busca um associado pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Associado encontrado"),
        @ApiResponse(responseCode = "404", description = "Associado não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Associado> buscarPorId(@PathVariable Long id) {
        return associadoService.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Lista todos os associados")
    @ApiResponse(responseCode = "200", description = "Lista de associados retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<Associado>> listarTodos() {
        return ResponseEntity.ok(associadoService.listarTodos());
    }
}