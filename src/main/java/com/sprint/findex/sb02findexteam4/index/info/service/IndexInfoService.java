package com.sprint.findex.sb02findexteam4.index.info.service;

import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateRequest;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IndexInfoService {
  IndexInfoDto registerIndexInfo(IndexInfoCreateRequest requestDto);
  Page<IndexInfoDto> getIndexInfoWithFilters(String classificationName, String indexName, Boolean favorite, Pageable pageable);
  IndexInfoDto findById(Long id);
  IndexInfoDto updateIndexInfo(Long id, IndexInfoUpdateRequest updateDto);
  void deleteIndexInfo(Long id);
}
