package com.sprint.findex.sb02findexteam4.index.info.controller;

import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateRequest;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoUpdateRequest;
import com.sprint.findex.sb02findexteam4.index.info.entity.SourceType;
import com.sprint.findex.sb02findexteam4.index.info.service.IndexInfoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/index-infos")
public class IndexInfoController {

    private final IndexInfoService indexInfoService;

    // 생성자 주입
    public IndexInfoController(IndexInfoService indexInfoService) {
        this.indexInfoService = indexInfoService;
    }

    @PostMapping
    public ResponseEntity<IndexInfoDto> createIndexInfo(@RequestBody IndexInfoCreateRequest indexInfoCreateRequest) {
        try {
            IndexInfoDto indexInfoDto = indexInfoService.registerIndexInfo(indexInfoCreateRequest, SourceType.USER);
            return ResponseEntity.status(HttpStatus.CREATED).body(indexInfoDto); // 201 Created
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 500 Internal Server Error
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<IndexInfoDto> findById(@PathVariable Long id) {
        IndexInfoDto indexInfoDto = indexInfoService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(indexInfoDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<IndexInfoDto> updateIndexInfo(@PathVariable Long id, @RequestBody IndexInfoUpdateRequest indexInfoUpdateRequest) {
        try {
            IndexInfoDto updatedIndexInfo = indexInfoService.updateIndexInfo(id, indexInfoUpdateRequest); // 반환 값 저장
            return ResponseEntity.ok(updatedIndexInfo); // 200 OK, 수정된 데이터 반환
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 500 Internal Server Error
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        try {
            indexInfoService.deleteIndexInfo(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 500 Internal Server Error
        }
    }
}
