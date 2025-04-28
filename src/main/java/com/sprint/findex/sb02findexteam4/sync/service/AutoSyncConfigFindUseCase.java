package com.sprint.findex.sb02findexteam4.sync.service;

import com.sprint.findex.sb02findexteam4.sync.dto.search.AutoSyncConfigCondition;
import com.sprint.findex.sb02findexteam4.sync.dto.CursorPageResponseAutoSyncConfigDto;

public interface AutoSyncConfigFindUseCase {

  CursorPageResponseAutoSyncConfigDto findAll(AutoSyncConfigCondition condition);
}
