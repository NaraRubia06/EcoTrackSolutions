package br.unipar.programacaoweb.ecotracksolutions.repository;

import br.unipar.programacaoweb.ecotracksolutions.model.LeituraSensor;
import br.unipar.programacaoweb.ecotracksolutions.model.LeituraSensor.TipoSensor;
import br.unipar.programacaoweb.ecotracksolutions.model.EstacaoMonitoramento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LeituraSensorRepository extends JpaRepository<LeituraSensor, Long> {

    List<LeituraSensor> findByEstacaoAndTipoSensorAndTimestampBetween(
            EstacaoMonitoramento estacao,
            TipoSensor tipoSensor,
            LocalDateTime start,
            LocalDateTime end
    );

    List<LeituraSensor> findByEstacao(EstacaoMonitoramento estacao);

    @Query("SELECT l FROM LeituraSensor l WHERE l.estacao.id = :idEstacao AND l.tipoSensor = :tipoSensor AND l.timestamp BETWEEN :inicio AND :fim")
    List<LeituraSensor> findByEstacaoAndTipoSensorAndPeriodo(
            @Param("idEstacao") Long idEstacao,
            @Param("tipoSensor") TipoSensor tipoSensor,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    @Query("SELECT MIN(l.valor), MAX(l.valor), AVG(l.valor) " +
            "FROM LeituraSensor l " +
            "WHERE l.estacao.id = :estacaoId " +
            "AND l.tipoSensor = :tipoSensor " +
            "AND l.timestamp BETWEEN :inicio AND :fim")
    Object[] buscarEstatisticas(@Param("estacaoId") Long estacaoId,
                                @Param("tipoSensor") LeituraSensor.TipoSensor tipoSensor,
                                @Param("inicio") LocalDateTime inicio,
                                @Param("fim") LocalDateTime fim);


    @Query("SELECT AVG(l.valor) FROM LeituraSensor l WHERE l.estacao.id = :idEstacao")
    Double findMediaGeralByEstacao(@Param("idEstacao") Long idEstacao);

    List<LeituraSensor> findByEstacaoIdAndTipoSensorAndTimestampBetween(Long estacaoId, TipoSensor tipo, LocalDateTime inicio, LocalDateTime fim);
}
