package com.sprint.findex.sb02findexteam4.index.info.repository;

import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoSummaryDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IndexInfoRepositoryCustom {

  Page<IndexInfoDto> getIndexInfoWithFilters(
      IndexInfoDto indexInfoDto,
      String sortProperty,
      boolean isAsc,
      Long cursorId,
      int pageSize);

  List<IndexInfoSummaryDto> getIndexInfoSummaries();
}