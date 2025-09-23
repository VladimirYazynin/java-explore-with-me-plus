package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.EndpointHit;
import ru.practicum.ewm.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query(value = "SELECT app, uri, COUNT(ip) as hits " +
            "FROM endpoint_hits " +
            "WHERE uri IN (:uris) AND timestamp BETWEEN :start AND :end " +
            "GROUP BY app, uri " +
            "ORDER BY hits DESC",
            nativeQuery = true)
    List<ViewStats> getByUris(@Param("uris") String[] uris,
                              @Param("start") LocalDateTime start,
                              @Param("end") LocalDateTime end);

    @Query(value = "SELECT app, uri, COUNT(ip) as hits " +
            "FROM endpoint_hits " +
            "WHERE timestamp BETWEEN :start AND :end " +
            "GROUP BY app, uri " +
            "ORDER BY hits DESC",
            nativeQuery = true)
    List<ViewStats> getByStartAndEnd(@Param("start") LocalDateTime start,
                                     @Param("end") LocalDateTime end);

    @Query(value = "SELECT app, uri, COUNT(DISTINCT ip) as hits " +
            "FROM endpoint_hits " +
            "WHERE timestamp BETWEEN :start AND :end " +
            "GROUP BY app, uri " +
            "ORDER BY hits DESC",
            nativeQuery = true)
    List<ViewStats> getDistinctByStartAndEnd(@Param("start") LocalDateTime start,
                                             @Param("end") LocalDateTime end);

    @Query(value = "SELECT app, uri, COUNT(DISTINCT ip) as hits " +
            "FROM endpoint_hits " +
            "WHERE uri IN (:uris) AND timestamp BETWEEN :start AND :end " +
            "GROUP BY app, uri " +
            "ORDER BY hits DESC",
            nativeQuery = true)
    List<ViewStats> getDistinctByUris(@Param("uris") String[] uris,
                                      @Param("start") LocalDateTime start,
                                      @Param("end") LocalDateTime end);
}
