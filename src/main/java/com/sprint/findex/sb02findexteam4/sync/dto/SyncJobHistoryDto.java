package com.sprint.findex.sb02findexteam4.sync.dto;

import com.sprint.findex.sb02findexteam4.sync.entity.JobResult;
import com.sprint.findex.sb02findexteam4.sync.entity.JobType;
import java.time.Instant;

public record SyncJobHistoryDto(
    Long id,
    JobType jobType,
    Long indexInfoId,
    Instant targetDate,
    String worker,
    Instant jobTime,
    JobResult result
) {
}