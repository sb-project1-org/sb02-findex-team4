package com.sprint.findex.sb02findexteam4.index.data.repository;

import com.sprint.findex.sb02findexteam4.index.data.entity.IndexData;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IndexDataRepository extends JpaRepository<IndexData, Long>,
    IndexDataRepositoryCustom {

  Optional<IndexData> findTopByIndexInfoIdOrderByBaseDateDesc(Long id);

  boolean existsByIndexInfoIdAndBaseDate(Long indexInfoId, Instant baseDate);

  Optional<IndexData> findByIndexInfoIdAndBaseDate(Long indexInfoId, Instant today);

  @Query(value = """
      SELECT *
      FROM   index_data
      WHERE  index_info_id = :id
      ORDER  BY ABS(EXTRACT(EPOCH FROM (base_date - :target)))   -- 거리(초) ASC
      LIMIT  1
      """, nativeQuery = true)
  Optional<IndexData> findClosestTo(Long id, LocalDate target);

  @Query("SELECT i FROM IndexData i " +
      "WHERE i.indexInfo.id = :indexInfoId " +
      "AND FUNCTION('DATE', i.baseDate) = :baseDate")
  Optional<IndexData> findByIndexInfoIdAndBaseDateOnlyDateMatch(
      @Param("indexInfoId") Long indexInfoId,
      @Param("baseDate") LocalDate baseDate
  );

  List<IndexData> findByIndexInfoIdInAndBaseDateBetween(List<Long> indexInfoIds,
      Instant baseDateFrom, Instant baseDateTo);
}
