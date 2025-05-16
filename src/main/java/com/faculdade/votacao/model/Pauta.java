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
@Table(name = "tb_pautas")
@Schema(description = "Entidade que representa uma pauta de votação")
public class Pauta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    @Schema(description = "Título da pauta", example = "Informe o Título da Pauta", required = true)
    @Column(nullable = false)
    private String titulo;

    @Schema(description = "Descrição detalhada da pauta", example = "Infome a descrição da Pauta")
    private String descricao;

    @JsonIgnore
    @OneToOne(mappedBy = "pauta", cascade = CascadeType.ALL)
    @Schema(hidden = true)
    private Sessao sessao;

    @JsonIgnore
    @OneToMany(mappedBy = "pauta")
    @Schema(hidden = true)
    private List<Voto> votos;
}