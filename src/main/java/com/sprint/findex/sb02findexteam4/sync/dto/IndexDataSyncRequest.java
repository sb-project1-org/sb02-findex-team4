package com.sprint.findex.sb02findexteam4.sync.dto;

import java.util.List;

public record IndexDataSyncRequest(
    List<Long> indexInfoIds,
    String baseDateFrom,
    String baseDateTo
) {

}
