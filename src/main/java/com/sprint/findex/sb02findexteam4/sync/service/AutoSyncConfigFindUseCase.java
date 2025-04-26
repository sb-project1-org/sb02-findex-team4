package com.sprint.findex.sb02findexteam4.sync.service;

import com.sprint.findex.sb02findexteam4.sync.dto.AutoSyncConfigCondition;
import com.sprint.findex.sb02findexteam4.sync.dto.CursorPageResponseAutoSyncConfigDto;

public interface AutoSyncConfigFindUseCase {

  CursorPageResponseAutoSyncConfigDto findAll(AutoSyncConfigCondition condition);
}
