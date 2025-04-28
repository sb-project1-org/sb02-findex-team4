package com.sprint.findex.sb02findexteam4.sync.service;

import com.sprint.findex.sb02findexteam4.sync.dto.CursorPageResponseSyncJobDto;
import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobHistoryCreateDto;
import com.sprint.findex.sb02findexteam4.sync.dto.search.SyncJobSearchCondition;
import com.sprint.findex.sb02findexteam4.sync.entity.JobResult;
import com.sprint.findex.sb02findexteam4.sync.entity.SyncJobHistory;
import com.sprint.findex.sb02findexteam4.sync.repository.SyncJobHistoryRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicSyncJobHistoryService implements SyncJobHistoryService {

  private final SyncJobHistoryRepository syncJobHistoryRepository;

  @Override
  public SyncJobHistory saveHistory(SyncJobHistoryCreateDto syncJobHistoryCreateDto) {
    SyncJobHistory syncJobHistory = SyncJobHistory.create(
        syncJobHistoryCreateDto.jobType(),
        syncJobHistoryCreateDto.indexInfo(),
        syncJobHistoryCreateDto.targetDate(),
        syncJobHistoryCreateDto.worker(),
        Instant.now(),
        JobResult.SUCCESS
    );

    return syncJobHistoryRepository.save(syncJobHistory);
  }

  @Override
  public CursorPageResponseSyncJobDto findHistoryByCursor(SyncJobSearchCondition condition) {
    return syncJobHistoryRepository.findSyncJobs(condition);
  }
}