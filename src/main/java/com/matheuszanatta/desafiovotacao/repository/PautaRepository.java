package com.matheuszanatta.desafiovotacao.repository;

import com.matheuszanatta.desafiovotacao.domain.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PautaRepository extends JpaRepository<Pauta, Long> {
}
