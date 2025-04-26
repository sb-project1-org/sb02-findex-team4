package com.sprint.findex.sb02findexteam4.index.info.controller;

import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateRequest;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoSummaryDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoUpdateRequest;
import com.sprint.findex.sb02findexteam4.index.info.entity.SourceType;
import com.sprint.findex.sb02findexteam4.index.info.service.IndexInfoService;
import jakarta.persistence.EntityNotFoundException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/index-infos")
public class IndexInfoController {

    private final IndexInfoService indexInfoService;

    public IndexInfoController(IndexInfoService indexInfoService) {
        this.indexInfoService = indexInfoService;
    }

    @PostMapping
    public ResponseEntity<IndexInfoDto> createIndexInfo(@RequestBody IndexInfoCreateRequest indexInfoCreateRequest) {
        IndexInfoDto indexInfoDto = indexInfoService.registerIndexInfo(indexInfoCreateRequest, SourceType.USER);
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

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> getIndexInfoWithFilters(
        @RequestParam(value = "indexClassification", required = false) String indexClassification,
        @RequestParam(value = "indexName", required = false) String indexName,
        @RequestParam(value = "favorite", required = false) Boolean favorite,
        @RequestParam(value = "sortProperty", defaultValue = "id") String sortProperty,
        @RequestParam(value = "isAsc", defaultValue = "true") boolean isAsc,
        @RequestParam(value = "cursorId", required = false) Long cursorId,
        @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {

        IndexInfoDto filterDto = new IndexInfoDto(
            null,
            indexClassification,
            indexName,
            null,
            null,
            null,
            null,
            favorite
        );

        Page<IndexInfoDto> resultPage = indexInfoService.getIndexInfoWithFilters(
            filterDto, sortProperty, isAsc, cursorId, pageSize);

        Map<String, Object> response = new HashMap<>();
        response.put("content", resultPage.getContent());
        response.put("size", resultPage.getSize());
        response.put("totalElements", resultPage.getTotalElements());
        response.put("hasNext", resultPage.hasNext());

        if (resultPage.hasNext()) {
            Long nextId = resultPage.getContent().get(resultPage.getContent().size() - 1).id();
            String nextCursor = encodeCursor(nextId);
            response.put("nextCursor", nextCursor);
            response.put("nextIdAfter", nextCursor);
        }

        return ResponseEntity.ok(response);
    }

    private String encodeCursor(Long id) {
        return Base64.getEncoder().encodeToString(("{\"id\":" + id + "}").getBytes());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<IndexInfoDto> updateIndexInfo(@PathVariable Long id, @RequestBody IndexInfoUpdateRequest indexInfoUpdateRequest) {
        IndexInfoDto updatedIndexInfo = indexInfoService.updateIndexInfo(id, indexInfoUpdateRequest);
        return ResponseEntity.ok(updatedIndexInfo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        indexInfoService.deleteIndexInfo(id);
        return ResponseEntity.noContent().build();
    }
}
