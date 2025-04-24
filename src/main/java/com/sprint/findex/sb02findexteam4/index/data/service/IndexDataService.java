package com.sprint.findex.sb02findexteam4.index.data.service;

import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataCreateRequest;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataResponse;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataUpdateRequest;
import com.sprint.findex.sb02findexteam4.index.info.entity.SourceType;
import java.time.Instant;


public interface IndexDataService {
  IndexDataResponse create(IndexDataCreateRequest request, SourceType sourceType);

  IndexDataResponse update(Long id, IndexDataUpdateRequest request);

  void delete(Long id);

  boolean isDuplicated(Long indexInfoId, Instant baseDate);
}
