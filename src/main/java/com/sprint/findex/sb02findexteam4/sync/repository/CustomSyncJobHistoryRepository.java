package com.sprint.findex.sb02findexteam4.sync.repository;

import com.sprint.findex.sb02findexteam4.sync.dto.CursorPageResponseSyncJobDto;
import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobSearchCondition;

public interface CustomSyncJobHistoryRepository {
    CursorPageResponseSyncJobDto findSyncJobs(SyncJobSearchCondition condition);
}
