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

        @Query("SELECT NEW ru.practicum.ewm.model.ViewStats(h.app, h.uri, COUNT(h.ip)) " +
                "FROM EndpointHit h " +
                "WHERE h.uri IN :uris AND h.timestamp BETWEEN :start AND :end " +
                "GROUP BY h.app, h.uri " +
                "ORDER BY COUNT(h.ip) DESC")
        List<ViewStats> getByUris(@Param("uris") String[] uris,
                                  @Param("start") LocalDateTime start,
                                  @Param("end") LocalDateTime end);

        @Query("SELECT NEW ru.practicum.ewm.model.ViewStats(h.app, h.uri, COUNT(h.ip)) " +
                "FROM EndpointHit h " +
                "WHERE h.timestamp BETWEEN :start AND :end " +
                "GROUP BY h.app, h.uri " +
                "ORDER BY COUNT(h.ip) DESC")
        List<ViewStats> getByStartAndEnd(@Param("start") LocalDateTime start,
                                         @Param("end") LocalDateTime end);

        @Query("SELECT NEW ru.practicum.ewm.model.ViewStats(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
                "FROM EndpointHit h " +
                "WHERE h.timestamp BETWEEN :start AND :end " +
                "GROUP BY h.app, h.uri " +
                "ORDER BY COUNT(DISTINCT h.ip) DESC")
        List<ViewStats> getDistinctByStartAndEnd(@Param("start") LocalDateTime start,
                                                 @Param("end") LocalDateTime end);

        @Query("SELECT NEW ru.practicum.ewm.model.ViewStats(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
                "FROM EndpointHit h " +
                "WHERE h.uri IN :uris AND h.timestamp BETWEEN :start AND :end " +
                "GROUP BY h.app, h.uri " +
                "ORDER BY COUNT(DISTINCT h.ip) DESC")
        List<ViewStats> getDistinctByUris(@Param("uris") String[] uris,
                                          @Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end);
    }
