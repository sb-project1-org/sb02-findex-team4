package com.sprint.findex.sb02findexteam4.sync.dto;

import com.sprint.findex.sb02findexteam4.sync.entity.JobResult;
import com.sprint.findex.sb02findexteam4.sync.entity.JobType;
import com.sprint.findex.sb02findexteam4.util.TimeUtils;
import java.time.Instant;

public record SyncJobSearchCondition(
    JobType jobType,
    Long indexInfoId,
    Instant baseDateFrom,
    Instant baseDateTo,
    String worker,
    Instant jobTimeFrom,
    Instant jobTimeTo,
    JobResult status,
    Long idAfter,
    String cursor,
    String sortField,
    String sortDirection,
    int size
) {
    public static SyncJobSearchCondition of(
        SyncJobSearchRequest request,
        String sortField,
        String sortDirection,
        int size
    ) {
        Instant baseDateFrom = request.baseDateFrom() != null
            ? TimeUtils.formatedTimeInstant(request.baseDateFrom())
            : null;

        Instant baseDateTo = request.baseDateTo() != null
            ? TimeUtils.formatedTimeInstant(request.baseDateTo())
            : null;

        return new SyncJobSearchCondition(
            request.jobType(),
            request.indexInfoId(),
            baseDateFrom,
            baseDateTo,
            request.worker(),
            request.jobTimeFrom(),
            request.jobTimeTo(),
            request.status(),
            request.idAfter(),
            request.cursor(),
            sortField,
            sortDirection,
            size
        );
    }

}
