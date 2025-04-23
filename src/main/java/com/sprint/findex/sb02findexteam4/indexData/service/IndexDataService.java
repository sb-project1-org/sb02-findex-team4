package com.sprint.findex.sb02findexteam4.indexData.service;

import com.sprint.findex.sb02findexteam4.indexData.dto.IndexDataCreateRequest;
import com.sprint.findex.sb02findexteam4.indexData.dto.IndexDataCreateResponse;
import com.sprint.findex.sb02findexteam4.indexInfo.SourceType;
import java.time.Instant;


public interface IndexDataService {
  IndexDataCreateResponse create(IndexDataCreateRequest request, SourceType sourceType);

  boolean isDuplicated(Long indexInfoId, Instant baseDate);

}
