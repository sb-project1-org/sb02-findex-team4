package com.sprint.findex.sb02findexteam4.indexInfo.service;

import com.sprint.findex.sb02findexteam4.indexInfo.dto.IndexInfoCreateRequestDto;
import com.sprint.findex.sb02findexteam4.indexInfo.dto.IndexInfoDto;
import com.sprint.findex.sb02findexteam4.indexInfo.dto.IndexInfoUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IndexInfoService {
  void registerIndexInfo(IndexInfoCreateRequestDto requestDto);
  Page<IndexInfoDto> getIndexInfoWithFilters(String classificationName, String indexName, Boolean favorite, Pageable pageable);
  void updateIndexInfo(Long id, IndexInfoUpdateRequestDto updateDto);
  void deleteIndexInfo(Long id);
}
