package com.sprint.findex.sb02findexteam4.indexInfo.service;

import com.sprint.findex.sb02findexteam4.indexInfo.dto.IndexInfoCreateRequest;
import com.sprint.findex.sb02findexteam4.indexInfo.dto.IndexInfoDto;
import com.sprint.findex.sb02findexteam4.indexInfo.dto.IndexInfoUpdateRequest;
import com.sprint.findex.sb02findexteam4.indexInfo.entity.IndexInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IndexInfoService {
  IndexInfoDto registerIndexInfo(IndexInfoCreateRequest requestDto);
  Page<IndexInfoDto> getIndexInfoWithFilters(String classificationName, String indexName, Boolean favorite, Pageable pageable);
  IndexInfoDto findById(Long id);
  IndexInfoDto updateIndexInfo(Long id, IndexInfoUpdateRequest updateDto);
  void deleteIndexInfo(Long id);
}
