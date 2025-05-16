package com.faculdade.votacao.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@Table(name = "tb_associados")
@Schema(description = "Entidade que representa um associado")
public class Associado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    @Schema(description = "Nome do associado", example = "Informe o nome", required = true)
    @Column(nullable = false)
    private String nome;

    @Schema(description = "CPF do associado", example = "Informe o CPF", required = true)
    @Column(unique = true, nullable = false)
    private String cpf;

    @JsonIgnore
    @OneToMany(mappedBy = "associado")
    @Schema(hidden = true)
    private List<Voto> votos;
}