package com.sprint.findex.sb02findexteam4.sync.service;

import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobHistoryCreateDto;
import com.sprint.findex.sb02findexteam4.sync.entity.SyncJobHistory;
import com.sprint.findex.sb02findexteam4.sync.repository.SyncJobHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicSyncJobHistoryService implements SyncJobHistoryService {

    private final SyncJobHistoryRepository syncJobHistoryRepository;

    @Override
    public SyncJobHistory saveHistory(SyncJobHistoryCreateDto syncJobHistoryCreateDto) {
        SyncJobHistory syncJobHistory = SyncJobHistory.create(
            syncJobHistoryCreateDto.jobType(),
            syncJobHistoryCreateDto.indexInfo(),
            syncJobHistoryCreateDto.targetDate(),
            syncJobHistoryCreateDto.worker(),
            syncJobHistoryCreateDto.jobTime(),
            syncJobHistoryCreateDto.result()
        );

        return syncJobHistoryRepository.save(syncJobHistory);
    }
}
