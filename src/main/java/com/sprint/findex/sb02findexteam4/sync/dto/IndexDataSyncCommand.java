package com.sprint.findex.sb02findexteam4.sync.dto;

import com.sprint.findex.sb02findexteam4.util.TimeUtils;
import java.time.Instant;
import java.util.List;

public record IndexDataSyncCommand(
    List<Long> indexInfoIds,
    Instant baseDateFrom,
    Instant baseDateTo
) {

  public static IndexDataSyncCommand toCommand(IndexDataSyncRequest request) {
    return new IndexDataSyncCommand(
        request.indexInfoIds(),
        TimeUtils.formatedTimeInstant(request.baseDateFrom()),
        TimeUtils.formatedTimeInstant(request.baseDateTo())
    );
  }
}
