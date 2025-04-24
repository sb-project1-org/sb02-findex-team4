package com.sprint.findex.sb02findexteam4.sync.dto;

import com.sprint.findex.sb02findexteam4.sync.entity.JobResult;
import com.sprint.findex.sb02findexteam4.sync.entity.JobType;
import java.time.Instant;

public record SyncJobSearchRequest(
    JobType jobType,
    Long indexInfoId,
    String baseDateFrom,
    String baseDateTo,
    String worker,
    Instant jobTimeFrom,
    Instant jobTimeTo,
    JobResult status,
    Long idAfter,
    String cursor
) {

}
