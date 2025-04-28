package com.sprint.findex.sb02findexteam4.sync.dto.search;

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
    Long cursor,
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
    Instant baseDateFrom = request.getBaseDateFrom() != null
        ? TimeUtils.formatedTimeInstant(request.getBaseDateFrom())
        : null;

    Instant baseDateTo = request.getBaseDateTo() != null
        ? TimeUtils.formatedTimeInstant(request.getBaseDateTo())
        : null;

    return new SyncJobSearchCondition(
        request.getJobType(),
        request.getIndexInfoId(),
        baseDateFrom,
        baseDateTo,
        request.getWorker(),
        request.getJobTimeFrom(),
        request.getJobTimeTo(),
        request.getStatus(),
        request.getIdAfter(),
        request.getCursor(),
        sortField,
        sortDirection,
        size
    );
  }

}
