package com.sprint.findex.sb02findexteam4.index.data.repository;

import com.sprint.findex.sb02findexteam4.index.data.IndexData;
import java.time.Instant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexDataRepository extends JpaRepository<IndexData, Long> {

  boolean existsByIndexInfoIdAndBaseDate(Long indexInfoId, Instant baseDate);
}
