package com.sprint.findex.sb02findexteam4.sync.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "자동 연동 설정 필터링들")
public class AutoSyncConfigSearchRequest {

  @Schema(description = "지수 정보 ID")
  Long indexInfoId;
  @Schema(description = "활성화 여부")
  boolean enabled;
  @Schema(description = "이전 페이지 마지막 요소 ID")
  Long idAfter;
  @Schema(description = "커서(다음 페이지 시작점)")
  String cursor;
}
