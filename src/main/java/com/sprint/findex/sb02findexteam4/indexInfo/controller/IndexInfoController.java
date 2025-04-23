package com.sprint.findex.sb02findexteam4.indexInfo.controller;

import com.sprint.findex.sb02findexteam4.indexInfo.IndexInfo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/index-infos")
@Controller
public class IndexInfoController {

    private final IndexInfoService indexInfoService;

    //생성자 주입
    public IndexInfoController(IndexInfoService indexInfoService) {
        this.indexInfoService = indexInfoService;
    }

    @PostMapping
    public ResponseEntity<IndexInfo> createIndexInfo(@RequestBody IndexInfoRequest IndexInfoRequest) {
        try {
            IndexInfo createdIndexInfo = indexInfoService.createIndexInfo(indexInfoRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdIndexInfo); //201 Created
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); //500 Internal Server Error
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<IndexInfo> updateIndexInfo(@PathVariable Long id, @RequestBody IndexInfoRequest indexInfoRequest) {
        try {
            IndexInfo updatedIndexInfo = indexInfoService.updateIndexInfo(id, indexInfoRequest);
            return ResponseEntity.ok(updatedIndexInfo); //200 ok
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); //404 Not Found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); //500 Internal Server Error
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<void> delete(@PathVariable long id) {
        try {
            indexInfoService.deleteIndexInfoById(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 500 Internal Server Error

        }
    }
}
