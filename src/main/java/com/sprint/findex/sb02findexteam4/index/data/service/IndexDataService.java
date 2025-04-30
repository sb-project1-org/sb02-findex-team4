package com.sprint.findex.sb02findexteam4.index.data.service;

import com.sprint.findex.sb02findexteam4.index.data.dto.CursorPageResponseIndexDataDto;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexChartDto;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataCreateRequest;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataCsvExportCommand;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataSearchCondition;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataResponse;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataUpdateRequest;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexPerformanceDto;
import com.sprint.findex.sb02findexteam4.index.data.dto.RankedIndexPerformanceDto;
import com.sprint.findex.sb02findexteam4.index.data.entity.PeriodType;
import java.util.List;


public interface IndexDataService {

  IndexDataResponse create(IndexDataCreateRequest request);

  IndexDataResponse update(Long id, IndexDataUpdateRequest request);

  CursorPageResponseIndexDataDto getIndexDataList(IndexDataSearchCondition command);

  byte[] exportCsv(IndexDataCsvExportCommand command);

  IndexChartDto getIndexChart(Long indexInfoId, PeriodType periodType);

  List<IndexPerformanceDto> getFavoriteIndexPerformances(PeriodType periodType);

  List<RankedIndexPerformanceDto> getIndexPerformanceRank(Long indexInfoId,
      PeriodType periodType, int limit);

  void delete(Long id);

}
