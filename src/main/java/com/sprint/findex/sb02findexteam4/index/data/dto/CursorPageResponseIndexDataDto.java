package com.sprint.findex.sb02findexteam4.index.data.dto;

import java.util.List;

public record CursorPageResponseIndexDataDto(
    List<IndexDataDto> content,
    Long nextIdAfter,
    String nextCursor,
    int size,
    long totalElements,
    boolean hasNext
) {
}

