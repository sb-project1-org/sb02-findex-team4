package com.sprint.findex.sb02findexteam4.sync.dto;

import com.sprint.findex.sb02findexteam4.sync.entity.AutoSyncConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "자동 연동 설정 DTO")
public record AutoSyncConfigDto(
    @Schema(description = "자동 연동 설정 ID", example = "1")
    Long id,
    @Schema(description = "지수 정보 ID", example = "1")
    Long indexInfoId,
    @Schema(description = "지수 분류명 ID", example = "KOSPI 시리즈")
    String indexClassification,
    @Schema(description = "지수명 ID", example = "IT 서비스")
    String indexName,
    @Schema(description = "활성화 여부 ID", example = "true")
    boolean enabled
) {

  public static AutoSyncConfigDto of(AutoSyncConfig config) {
    return AutoSyncConfigDto.builder()
        .id(config.getId())
        .enabled(config.isEnabled())
        .indexInfoId(config.getIndexInfo().getId())
        .indexClassification(config.getIndexInfo().getIndexClassification())
        .indexName(config.getIndexInfo().getIndexName()).build();
  }
}
