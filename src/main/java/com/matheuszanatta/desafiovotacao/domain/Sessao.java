package com.matheuszanatta.desafiovotacao.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static java.time.LocalDateTime.now;

@AllArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(of = "id")
@Getter
@NoArgsConstructor
@Setter
@ToString(of = "id")
public class Sessao {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pauta_id", nullable = false)
    private Pauta pauta;

    @Column(nullable = false)
    private LocalDateTime dataInicial;

    @Column(nullable = false)
    private LocalDateTime dataFinal;

    @Column(nullable = false)
    private boolean enviada;

    public boolean isEncerrada() {
        return this.getDataFinal().isBefore(now());
    }

    public boolean isAberta() {
        return !this.isEncerrada();
    }
}
