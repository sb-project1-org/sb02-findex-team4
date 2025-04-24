package com.sprint.findex.sb02findexteam4.sync.dto;

import com.sprint.findex.sb02findexteam4.index.info.entity.IndexInfo;
import com.sprint.findex.sb02findexteam4.sync.entity.JobType;
import java.time.Instant;

public record SyncJobHistoryCreateDto(
    JobType jobType,
    IndexInfo indexInfo,
    Instant targetDate,
    String worker
) {
    public static SyncJobHistoryCreateDto forIndexInfo(IndexInfo indexInfo, String worker) {
        return new SyncJobHistoryCreateDto(JobType.INDEX_INFO, indexInfo, null, worker);
    }

    public static SyncJobHistoryCreateDto forIndexData(IndexInfo indexInfo, String worker, Instant targetDate) {
        if (targetDate == null) {
            throw new IllegalArgumentException("INDEX_DATA 작업에서는 targetDate가 필요합니다.");
        }
        return new SyncJobHistoryCreateDto(JobType.INDEX_DATA, indexInfo, targetDate, worker);
    }
}