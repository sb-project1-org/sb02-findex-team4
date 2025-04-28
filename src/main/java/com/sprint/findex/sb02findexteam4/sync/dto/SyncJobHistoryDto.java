package com.sprint.findex.sb02findexteam4.sync.dto;

import com.sprint.findex.sb02findexteam4.sync.entity.JobResult;
import com.sprint.findex.sb02findexteam4.sync.entity.JobType;
import com.sprint.findex.sb02findexteam4.sync.entity.SyncJobHistory;
import com.sprint.findex.sb02findexteam4.util.TimeUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.Builder;

@Builder
@Schema(description = "연동 작업 이력 DTO")
public record SyncJobHistoryDto(
    @Schema(description = "연동 작업 이력 ID", example = "1")
    Long id,
    @Schema(
        description = "연동 작업 유형",
        allowableValues = {"INDEX_INFO", "INDEX_DATA"},
        example = "INDEX_DATA"
    )
    JobType jobType,
    @Schema(description = "지수 정보 ID", example = "1")
    Long indexInfoId,
    @Schema(
        description = "대상 날짜 (지수 데이터 연동인 경우만 존재)",
        example = "2025-01-01",
        type = "string",
        format = "date"
    )
    String targetDate,
    @Schema(description = "작업자 (IP 또는 system)", example = "192.168.0.1")
    String worker,
    @Schema(
        description = "작업 시간",
        example = "2025-01-01T12:00:00",
        type = "string",
        format = "date-time"
    )
    Instant jobTime,
    @Schema(
        description = "작업 결과",
        allowableValues = {"SUCCESS", "FAILED"},
        example = "SUCCESS"
    )
    JobResult result
) {

  public static SyncJobHistoryDto fromIndexInfo(SyncJobHistory syncJobHistory) {
    return SyncJobHistoryDto.builder()
        .id(syncJobHistory.getId())
        .jobType(syncJobHistory.getJobType())
        .indexInfoId(syncJobHistory.getIndexInfo().getId())
        .worker(syncJobHistory.getWorker())
        .jobTime(syncJobHistory.getJobTime())
        .result(syncJobHistory.getJobResult())
        .build();
  }

  public static SyncJobHistoryDto fromIndexData(SyncJobHistory syncJobHistory) {
    return SyncJobHistoryDto.builder()
        .id(syncJobHistory.getId())
        .jobType(syncJobHistory.getJobType())
        .targetDate(TimeUtils.formatedTimeString(syncJobHistory.getTargetDate()))
        .indexInfoId(syncJobHistory.getIndexInfo().getId())
        .worker(syncJobHistory.getWorker())
        .jobTime(syncJobHistory.getJobTime())
        .result(syncJobHistory.getJobResult())
        .build();
  }

}