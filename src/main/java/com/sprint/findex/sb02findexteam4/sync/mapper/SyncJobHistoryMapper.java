package com.sprint.findex.sb02findexteam4.sync.mapper;

import com.sprint.findex.sb02findexteam4.sync.dto.CursorPageResponseSyncJobDto;
import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobHistoryDto;
import com.sprint.findex.sb02findexteam4.sync.entity.SyncJobHistory;
import com.sprint.findex.sb02findexteam4.util.TimeUtils;
import java.time.Instant;
import java.util.List;

public class SyncJobHistoryMapper {

  public static SyncJobHistoryDto toDto(SyncJobHistory syncJobHistory) {
    Instant targetDate = syncJobHistory.getTargetDate();
    if (targetDate == null) {
      return new SyncJobHistoryDto(
          syncJobHistory.getId(),
          syncJobHistory.getJobType(),
          syncJobHistory.getIndexInfo().getId(),
          null,
          syncJobHistory.getWorker(),
          syncJobHistory.getJobTime(),
          syncJobHistory.getJobResult()
      );
    } else {
      return new SyncJobHistoryDto(
          syncJobHistory.getId(),
          syncJobHistory.getJobType(),
          syncJobHistory.getIndexInfo().getId(),
          TimeUtils.formatedTimeString(targetDate),
          syncJobHistory.getWorker(),
          syncJobHistory.getJobTime(),
          syncJobHistory.getJobResult()
      );
    }
  }

  public static CursorPageResponseSyncJobDto toCursorPageResponseDto(
      List<SyncJobHistoryDto> content, boolean hasNext, Long totalElements) {
    SyncJobHistoryDto last = content.isEmpty() ? null : content.get(content.size() - 1);

    String nextCursor = hasNext && last != null ? last.jobTime().toString() : null;
    Long nextIdAfter = hasNext && last != null ? last.id() : null;

    return new CursorPageResponseSyncJobDto(
        content,
        nextCursor,
        nextIdAfter,
        content.size(),
        totalElements,
        hasNext
    );
  }
}
