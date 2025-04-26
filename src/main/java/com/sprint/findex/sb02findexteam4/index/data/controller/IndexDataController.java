package com.sprint.findex.sb02findexteam4.index.data.controller;


import com.sprint.findex.sb02findexteam4.index.data.dto.CursorPageResponseIndexDataDto;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexChartDto;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataCreateRequest;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataFindCommand;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataResponse;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataUpdateRequest;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexPerformanceDto;
import com.sprint.findex.sb02findexteam4.index.data.dto.RankedIndexPerformanceDto;
import com.sprint.findex.sb02findexteam4.index.data.entity.PeriodType;
import com.sprint.findex.sb02findexteam4.index.data.service.IndexDataService;
import com.sprint.findex.sb02findexteam4.index.info.entity.SourceType;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/index-data")
public class IndexDataController {

  private final IndexDataService indexDataService;

  public IndexDataController(IndexDataService indexDataService) {
    this.indexDataService = indexDataService;
  }

  @PostMapping
  public ResponseEntity<IndexDataResponse> createIndexData(
      @RequestBody IndexDataCreateRequest request) {
    IndexDataResponse createdIndexData = indexDataService.create(request, SourceType.USER);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdIndexData); //201 Created
  }

  @PatchMapping("/{id}")
  public ResponseEntity<IndexDataResponse> updateIndexData(@PathVariable Long id,
      @RequestBody IndexDataUpdateRequest indexDataRequest) {
    IndexDataResponse updatedIndexData = indexDataService.update(id, indexDataRequest);
    return ResponseEntity.ok(updatedIndexData);
  }

  @GetMapping
  public ResponseEntity<CursorPageResponseIndexDataDto> getIndexDataList(
      @RequestParam(required = false) Long indexInfoId,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate,
      @RequestParam(required = false) Long idAfter,
      @RequestParam(required = false) String cursor,
      @RequestParam(defaultValue = "baseDate") String sortField,
      @RequestParam(defaultValue = "desc") String sortDirection,
      @RequestParam(defaultValue = "10") int size
  ) {
    IndexDataFindCommand command = new IndexDataFindCommand(
        indexInfoId, startDate, endDate, idAfter, cursor, sortField, sortDirection, size
    );
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

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteIndexData(@PathVariable long id) {
    indexDataService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
