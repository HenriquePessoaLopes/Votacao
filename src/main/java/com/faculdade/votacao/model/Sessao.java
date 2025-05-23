package com.faculdade.votacao.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_sessoes")
@Getter
@Setter
public class Sessao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "pauta_id")
    private Pauta pauta;

    private LocalDateTime dataAbertura;

    private LocalDateTime dataFechamento;

    public Sessao() {

    }

    @Override
    public String toString() {
        return "Sessao [id=" + id + ", pautaId=" + (pauta != null ? pauta.getId() : null) + "]";
    }

    public boolean isAberta() {
        LocalDateTime agora = LocalDateTime.now();
        return agora.isAfter(dataAbertura) && agora.isBefore(dataFechamento);
    }

    public boolean isAgendada() {
        LocalDateTime agora = LocalDateTime.now();
        return agora.isBefore(dataAbertura);
    }

    public boolean isEncerrada() {
        LocalDateTime agora = LocalDateTime.now();
        return agora.isAfter(dataFechamento);
    }

    public String getStatus() {
        if (isAgendada()) {
            return "AGENDADA";
        } else if (isAberta()) {
            return "ABERTA";
        } else {
            return "ENCERRADA";
        }
    }
}