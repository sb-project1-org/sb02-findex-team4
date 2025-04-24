package com.sprint.findex.sb02findexteam4.sync.repository;

import com.sprint.findex.sb02findexteam4.sync.entity.AutoSyncConfig;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AutoSyncConfigRepository extends JpaRepository<AutoSyncConfig, Long> {

  boolean existsByIndexInfo_Id(Long indexInfoId);

  void deleteByIndexInfo_Id(Long indexInfoId);

  @Query("SELECT a FROM AutoSyncConfig a WHERE a.enabled = true")
  List<AutoSyncConfig> findAllByEnabledIsTrue();

  Slice<AutoSyncConfig> findAllByIndexInfo_Id(Long indexInfoId, Pageable pageable);

  Slice<AutoSyncConfig> findAllByEnabled(boolean enabled, Pageable pageable);

  Slice<AutoSyncConfig> findAllByIndexInfoIdAndEnabled(Long indexInfoId, boolean enabled,
      Pageable pageable);

}
