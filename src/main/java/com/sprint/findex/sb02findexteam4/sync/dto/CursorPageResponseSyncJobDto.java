package com.sprint.findex.sb02findexteam4.sync.dto;


import java.util.List;

public record CursorPageResponseSyncJobDto(
    List<SyncJobHistoryDto> content,
    Long nextCursor,
    Long nextIdAfter,
    int size,
    Long totalElements,
    boolean hasNext
) {

}
