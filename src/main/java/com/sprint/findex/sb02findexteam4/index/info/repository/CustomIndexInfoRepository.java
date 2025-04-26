package com.sprint.findex.sb02findexteam4.index.info.repository;

import com.sprint.findex.sb02findexteam4.index.info.dto.CursorPageResponseIndexInfoDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoSearchCondition;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoSummaryDto;
import java.util.List;
import org.springframework.data.domain.Page;

public interface CustomIndexInfoRepository {

  Page<IndexInfoDto> getIndexInfoWithFilters(
      IndexInfoDto indexInfoDto,
      String sortProperty,
      boolean isAsc,
      Long cursorId,
      int pageSize);

  List<IndexInfoSummaryDto> getIndexInfoSummaries();

  CursorPageResponseIndexInfoDto findIndexInfo(IndexInfoSearchCondition condition);
}
