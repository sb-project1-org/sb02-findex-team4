package com.sprint.findex.sb02findexteam4.indexData.service;

import com.sprint.findex.sb02findexteam4.indexData.dto.IndexDataCreateRequest;
import com.sprint.findex.sb02findexteam4.indexData.dto.IndexDataResponse;
import com.sprint.findex.sb02findexteam4.indexData.dto.IndexDataUpdateRequest;
import com.sprint.findex.sb02findexteam4.indexInfo.SourceType;
import java.time.Instant;


public interface IndexDataService {
  IndexDataResponse create(IndexDataCreateRequest request, SourceType sourceType);

  IndexDataResponse update(Long id, IndexDataUpdateRequest request);

  void delete(Long id);

  boolean isDuplicated(Long indexInfoId, Instant baseDate);
}
