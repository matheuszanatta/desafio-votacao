package com.matheuszanatta.desafiovotacao.repository;

import com.matheuszanatta.desafiovotacao.domain.Associado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssociadoRepository extends JpaRepository<Associado, Long> {
    boolean existsByCpf(String cpf);
}
