package com.sprint.findex.sb02findexteam4.sync.service;

import com.sprint.findex.sb02findexteam4.sync.dto.IndexDataSyncCommand;
import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobHistoryDto;
import java.util.List;

public interface SyncJobUserUseCase {

  List<SyncJobHistoryDto> syncIndexInfo(String ip);

  List<SyncJobHistoryDto> syncIndexData(IndexDataSyncCommand request, String ip);
}
