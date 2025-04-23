package com.sprint.findex.sb02findexteam4.sync.service;

import com.sprint.findex.sb02findexteam4.sync.dto.CursorPageResponseSyncJobDto;
import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobHistoryDto;
import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobSearchCondition;
import com.sprint.findex.sb02findexteam4.sync.repository.SyncJobHistoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicSyncJobService implements SyncJobService {

    private final SyncJobHistoryRepository syncJobHistoryRepository;

    @Override
    public List<SyncJobHistoryDto> syncIndexInfos() {
        return List.of();
    }

    @Override
    public CursorPageResponseSyncJobDto getSyncJobs(SyncJobSearchCondition condition) {
        return syncJobHistoryRepository.findSyncJobs(condition);
    }
}
