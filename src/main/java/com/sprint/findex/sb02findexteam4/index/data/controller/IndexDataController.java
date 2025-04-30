package com.sprint.findex.sb02findexteam4.index.data.controller;


import com.sprint.findex.sb02findexteam4.index.data.dto.CursorPageResponseIndexDataDto;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexChartDto;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataCreateRequest;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataCsvExportCommand;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataResponse;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataSearchCondition;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataUpdateRequest;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexPerformanceDto;
import com.sprint.findex.sb02findexteam4.index.data.dto.RankedIndexPerformanceDto;
import com.sprint.findex.sb02findexteam4.index.data.entity.PeriodType;
import com.sprint.findex.sb02findexteam4.index.data.service.IndexDataService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/index-data")
public class IndexDataController {

  private final IndexDataService indexDataService;

  @PostMapping
  public ResponseEntity<IndexDataResponse> createIndexData(
      @RequestBody IndexDataCreateRequest request) {

    IndexDataResponse createdIndexData = indexDataService.create(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(createdIndexData);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<IndexDataResponse> updateIndexData(@PathVariable Long id,
      @RequestBody IndexDataUpdateRequest indexDataRequest) {
    IndexDataResponse updatedIndexData = indexDataService.update(id, indexDataRequest);
    return ResponseEntity.ok(updatedIndexData);
  }

  @GetMapping
  public ResponseEntity<CursorPageResponseIndexDataDto> getIndexDataList(
      IndexDataSearchCondition command) {
    return ResponseEntity.ok(indexDataService.getIndexDataList(command));
  }

  @GetMapping("/{id}/chart")
  public ResponseEntity<IndexChartDto> getChartData(
      @PathVariable("id") Long indexInfoId,
      @RequestParam(value = "periodType", defaultValue = "DAILY") PeriodType periodType) {

    IndexChartDto chartData = indexDataService.getIndexChart(indexInfoId, periodType);
    return ResponseEntity.ok(chartData);
  }

  @GetMapping("/performance/favorite")
  public ResponseEntity<List<IndexPerformanceDto>> getFavoriteIndexPerformances(
      @RequestParam(defaultValue = "DAILY") PeriodType periodType) {
    List<IndexPerformanceDto> result = indexDataService.getFavoriteIndexPerformances(periodType);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/performance/rank")
  public ResponseEntity<List<RankedIndexPerformanceDto>> getPerformanceRank(
      @RequestParam(required = false) Long indexInfoId,
      @RequestParam(defaultValue = "DAILY") PeriodType periodType,
      @RequestParam(defaultValue = "10") int limit
  ) {
    List<RankedIndexPerformanceDto> result = indexDataService.getIndexPerformanceRank(indexInfoId,
        periodType, limit);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/export/csv")
  public ResponseEntity<byte[]> exportCsv(IndexDataCsvExportCommand command) {
    byte[] csvData = indexDataService.exportCsv(command);

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=index-data-export.csv")
        .contentType(MediaType.parseMediaType("text/csv"))
        .body(csvData);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteIndexData(@PathVariable long id) {
    indexDataService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
