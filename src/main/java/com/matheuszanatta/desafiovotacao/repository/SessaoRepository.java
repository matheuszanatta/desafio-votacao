package com.matheuszanatta.desafiovotacao.repository;

import com.matheuszanatta.desafiovotacao.domain.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SessaoRepository extends JpaRepository<Sessao, Long> {

    boolean existsByPautaId(Long idPauta);

    @Query("select s from Sessao s join fetch s.pauta where s.pauta.id = :idPauta")
    Optional<Sessao> findByPautaIdFetch(Long idPauta);

    @Query("select s from Sessao s join fetch s.pauta where s.dataFinal <= NOW() and s.enviada = false")
    List<Sessao> buscarSessoesEncerradasNaoEnviadas();
}
