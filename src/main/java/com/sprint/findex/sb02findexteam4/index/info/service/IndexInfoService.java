package com.sprint.findex.sb02findexteam4.index.info.service;

import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateCommand;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateRequest;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoSummaryDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoUpdateRequest;
import com.sprint.findex.sb02findexteam4.index.info.entity.IndexInfo;
import com.sprint.findex.sb02findexteam4.index.info.entity.SourceType;
import java.util.List;
import org.springframework.data.domain.Page;

public interface IndexInfoService {

  IndexInfoDto registerIndexInfo(IndexInfoCreateRequest requestDto, SourceType sourceType);
  IndexInfo registerIndexInfoFromApi(IndexInfoCreateCommand command);
  Page<IndexInfoDto> getIndexInfoWithFilters(
      IndexInfoDto indexInfoDto,
      String sortProperty,
      boolean isAsc,
      Long cursorId,
      int pageSize);
  List<IndexInfoSummaryDto> getIndexInfoSummaries();
  IndexInfoDto findById(Long id);
  IndexInfoDto updateIndexInfo(Long id, IndexInfoUpdateRequest updateDto);
  void deleteIndexInfo(Long id);
}