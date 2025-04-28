package com.sprint.findex.sb02findexteam4.index.info.controller;

import com.sprint.findex.sb02findexteam4.index.info.dto.CursorPageResponseIndexInfoDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateCommand;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateRequest;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoSearchCondition;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoSearchRequest;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoSummaryDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoUpdateRequest;
import com.sprint.findex.sb02findexteam4.index.info.service.IndexInfoService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/index-infos")
public class IndexInfoController {

  private final IndexInfoService indexInfoService;

  public IndexInfoController(IndexInfoService indexInfoService) {
    this.indexInfoService = indexInfoService;
  }

  @PostMapping
  public ResponseEntity<IndexInfoDto> createIndexInfo(
      @RequestBody IndexInfoCreateRequest indexInfoCreateRequest) {
    IndexInfoCreateCommand command = IndexInfoCreateCommand.fromUser(
        indexInfoCreateRequest);
    IndexInfoDto indexInfoDto = indexInfoService.registerIndexInfo(command);
    return ResponseEntity.status(HttpStatus.CREATED).body(indexInfoDto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<IndexInfoDto> findById(@PathVariable Long id) {
    IndexInfoDto indexInfoDto = indexInfoService.findById(id);
    return ResponseEntity.ok(indexInfoDto);
  }

  @GetMapping("/summaries")
  public ResponseEntity<List<IndexInfoSummaryDto>> getIndexInfoSummaries() {
    List<IndexInfoSummaryDto> summaries = indexInfoService.getIndexInfoSummaries();
    return ResponseEntity.ok(summaries);
  }

  @GetMapping
  public ResponseEntity<CursorPageResponseIndexInfoDto> getIndexInfoWithFilters(
      @ModelAttribute IndexInfoSearchRequest request,
      @RequestParam(defaultValue = "indexClassification", required = false) String sortField,
      @RequestParam(defaultValue = "asc", required = false) String sortDirection,
      @RequestParam(defaultValue = "10", required = false) int size,
      HttpServletRequest httpRequest
  ) {

    boolean favoriteProvided = httpRequest.getParameterMap().containsKey("favorite");

    Boolean favoriteCond = favoriteProvided ? request.isFavorite() : null;
    log.info("Controller: Index Info 전체 검색 -> indexClassification {}, indexName {} ",
        request.getIndexClassification(), request.getIndexName());
    IndexInfoSearchCondition indexInfoSearchCondition = IndexInfoSearchCondition.of(request,
        favoriteCond,
        sortField, sortDirection, size);

    CursorPageResponseIndexInfoDto indexInfoByCursor = indexInfoService.findIndexInfoByCursor(
        indexInfoSearchCondition);

    return ResponseEntity.ok(indexInfoByCursor);
  }

  private String encodeCursor(Long id) {
    return Base64.getEncoder().encodeToString(("{\"id\":" + id + "}").getBytes());
  }

  @PatchMapping("/{id}")
  public ResponseEntity<IndexInfoDto> updateIndexInfo(@PathVariable Long id,
      @RequestBody IndexInfoUpdateRequest indexInfoUpdateRequest) {
    IndexInfoDto updatedIndexInfo = indexInfoService.updateIndexInfo(id, indexInfoUpdateRequest);
    return ResponseEntity.ok(updatedIndexInfo);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable long id) {
    indexInfoService.deleteIndexInfo(id);
    return ResponseEntity.noContent().build();
  }
}
