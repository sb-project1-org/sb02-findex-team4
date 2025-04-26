package com.sprint.findex.sb02findexteam4.index.info.service;

import com.sprint.findex.sb02findexteam4.index.info.dto.CursorPageResponseIndexInfoDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateCommand;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateRequest;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoSearchCondition;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoSummaryDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoUpdateRequest;
import com.sprint.findex.sb02findexteam4.index.info.entity.IndexInfo;
import com.sprint.findex.sb02findexteam4.index.info.entity.SourceType;
import java.util.List;

public interface IndexInfoService {

  IndexInfoDto registerIndexInfo(IndexInfoCreateRequest requestDto, SourceType sourceType);

  IndexInfo registerIndexInfoFromApi(IndexInfoCreateCommand command);

  CursorPageResponseIndexInfoDto findIndexInfoByCursor(IndexInfoSearchCondition condition);

  List<IndexInfoSummaryDto> getIndexInfoSummaries();

  IndexInfoDto findById(Long id);

  IndexInfoDto updateIndexInfo(Long id, IndexInfoUpdateRequest updateDto);

  void deleteIndexInfo(Long id);
}