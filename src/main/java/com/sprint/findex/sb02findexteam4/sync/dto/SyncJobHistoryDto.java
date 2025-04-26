package com.sprint.findex.sb02findexteam4.sync.dto;

import com.sprint.findex.sb02findexteam4.sync.entity.JobResult;
import com.sprint.findex.sb02findexteam4.sync.entity.JobType;
import com.sprint.findex.sb02findexteam4.sync.entity.SyncJobHistory;
import com.sprint.findex.sb02findexteam4.util.TimeUtils;
import java.time.Instant;
import lombok.Builder;

@Builder
public record SyncJobHistoryDto(
    Long id,
    JobType jobType,
    Long indexInfoId,
    String targetDate,
    String worker,
    Instant jobTime,
    JobResult result
) {

  public static SyncJobHistoryDto fromIndexInfo(SyncJobHistory syncJobHistory) {
    return SyncJobHistoryDto.builder()
        .id(syncJobHistory.getId())
        .jobType(syncJobHistory.getJobType())
        .indexInfoId(syncJobHistory.getIndexInfo().getId())
        .worker(syncJobHistory.getWorker())
        .jobTime(syncJobHistory.getJobTime())
        .result(syncJobHistory.getJobResult())
        .build();
  }

  public static SyncJobHistoryDto fromIndexData(SyncJobHistory syncJobHistory) {
    return SyncJobHistoryDto.builder()
        .id(syncJobHistory.getId())
        .jobType(syncJobHistory.getJobType())
        .targetDate(TimeUtils.formatedTimeString(syncJobHistory.getTargetDate()))
        .indexInfoId(syncJobHistory.getIndexInfo().getId())
        .worker(syncJobHistory.getWorker())
        .jobTime(syncJobHistory.getJobTime())
        .result(syncJobHistory.getJobResult())
        .build();
  }

}