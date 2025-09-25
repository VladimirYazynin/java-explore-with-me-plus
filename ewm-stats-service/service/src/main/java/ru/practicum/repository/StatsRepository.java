package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("select new ru.practicum.model.ViewStats (e.app, e.uri, count(e.ip) as hits) from EndpointHit e " +
            "where e.uri in (?1) and e.timestamp between ?2 and ?3 group by e.ip, e.uri, e.app order by hits desc")
    List<ViewStats> getByUris(String[] uris, LocalDateTime start, LocalDateTime end);

    List<ViewStats> findAllByUriIn(String[] uris);

    @Query("select new ru.practicum.model.ViewStats (e.app, e.uri, count(e.ip) as hits) from EndpointHit e " +
            "where e.timestamp between ?1 and ?2 group by e.ip, e.uri, e.app order by hits desc")
    List<ViewStats> getByStartAndEnd(LocalDateTime start, LocalDateTime end);

    @Query("select distinct new ru.practicum.model.ViewStats (e.app, e.uri, count(distinct e.ip) as hits) " +
            "from EndpointHit e where e.timestamp between ?1 and ?2 group by e.ip, e.uri, e.app order by hits desc")
    List<ViewStats> getDistinctByStartAndEnd(LocalDateTime start, LocalDateTime end);

    @Query("select distinct new ru.practicum.model.ViewStats (e.app, e.uri, count(distinct e.ip) as hits) " +
            "from EndpointHit e where e.uri in (?1) and e.timestamp between ?2 and ?3 group by e.ip, e.uri, e.app " +
            "order by hits desc")
    List<ViewStats> getDistinctByUris(String[] uris, LocalDateTime start, LocalDateTime end);
}
