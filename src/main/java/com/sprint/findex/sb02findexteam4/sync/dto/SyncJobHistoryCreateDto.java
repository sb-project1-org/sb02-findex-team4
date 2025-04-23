package com.sprint.findex.sb02findexteam4.sync.dto;

import com.sprint.findex.sb02findexteam4.indexInfo.IndexInfo;
import com.sprint.findex.sb02findexteam4.sync.entity.JobResult;
import com.sprint.findex.sb02findexteam4.sync.entity.JobType;
import java.time.Instant;

public record SyncJobHistoryCreateDto(
    JobType jobType,
    IndexInfo indexInfo,
    Instant targetDate,
    String worker,
    Instant jobTime,
    JobResult result
) {
}
