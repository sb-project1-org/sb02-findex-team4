package com.sprint.findex.sb02findexteam4.sync.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "자동 연동 설정 수정 요청")
public record AutoSyncConfigUpdateRequest(
    @Schema(description = "활성화 여부", example = "true")
    boolean enabled
) {

}
