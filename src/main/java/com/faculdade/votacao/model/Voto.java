package com.faculdade.votacao.model;

import java.time.LocalDateTime;

import com.faculdade.votacao.enums.OpcaoVoto;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_votos", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"associado_id", "pauta_id"})
})
@Getter
@Setter
public class Voto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "pauta_id")
    private Pauta pauta;

    @ManyToOne
    @JoinColumn(name = "associado_id")
    private Associado associado;
    
    @Enumerated(EnumType.STRING) 
    private OpcaoVoto votoFavoravel;  
    
    private LocalDateTime dataVoto;

    public Voto() {
        
    }

    public Voto(Long id, Associado associado, Pauta pauta, OpcaoVoto votoFavoravel, LocalDateTime dataVoto) {
        this.id = id;
        this.associado = associado;
        this.pauta = pauta;
        this.votoFavoravel = votoFavoravel;
        this.dataVoto = dataVoto;
    }
    
    @Override
    public String toString() {
        return "Voto [id=" + id + 
               ", pautaId=" + (pauta != null ? pauta.getId() : null) + 
               ", associadoId=" + (associado != null ? associado.getId() : null) + 
               ", opcao=" + votoFavoravel + "]";
    }
}