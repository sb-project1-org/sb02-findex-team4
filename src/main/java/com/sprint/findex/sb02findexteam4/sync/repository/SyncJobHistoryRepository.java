package com.sprint.findex.sb02findexteam4.sync.repository;

import com.sprint.findex.sb02findexteam4.sync.entity.SyncJobHistory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SyncJobHistoryRepository extends JpaRepository<SyncJobHistory, Long>, CustomSyncJobHistoryRepository{
    Optional<SyncJobHistory> findTopByOrderByJobTimeDesc();
}