package com.sprint.findex.sb02findexteam4.index.info.dto;

import java.util.List;

public record CursorPageResponseIndexInfoDto(
    List<IndexInfoDto> content,
    String nextCursor,
    Long nextIdAfter,
    int size,
    Long totalElements,
    boolean hasNext
) {

}
