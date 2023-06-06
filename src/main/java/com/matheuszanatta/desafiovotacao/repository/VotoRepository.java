package com.matheuszanatta.desafiovotacao.repository;

import com.matheuszanatta.desafiovotacao.domain.Voto;
import com.matheuszanatta.desafiovotacao.repository.projection.VotoResultado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    boolean existsByAssociadoIdAndPautaId(Long idAssociado, Long idPauta);

    @Query("SELECT v.votoComputado AS computado, COUNT(v.id) AS total " +
            "FROM Voto v " +
            "WHERE v.pauta.id = :pautaId " +
            "GROUP BY v.votoComputado")
    List<VotoResultado> buscarVotosComputadosPorPauta(@Param("pautaId") Long pautaId);
}
