package com.sprint.findex.sb02findexteam4.sync.repository;

import com.sprint.findex.sb02findexteam4.sync.dto.AutoSyncConfigCondition;
import com.sprint.findex.sb02findexteam4.sync.dto.CursorPageResponseAutoSyncConfigDto;

public interface CustomAutoSyncConfigRepository {

  CursorPageResponseAutoSyncConfigDto findAutoSyncConfig(AutoSyncConfigCondition condition);
}
