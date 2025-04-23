package com.sprint.findex.sb02findexteam4.sync.service;

import com.sprint.findex.sb02findexteam4.sync.dto.CursorPageResponseSyncJobDto;
import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobHistoryDto;
import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobSearchCondition;
import java.util.List;

public interface SyncJobService {
    List<SyncJobHistoryDto> syncIndexInfos();

    CursorPageResponseSyncJobDto getSyncJobs(SyncJobSearchCondition condition);
}
