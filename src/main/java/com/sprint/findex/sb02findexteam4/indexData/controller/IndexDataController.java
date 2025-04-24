package com.sprint.findex.sb02findexteam4.indexData.controller;


import com.sprint.findex.sb02findexteam4.indexData.dto.IndexDataCreateRequest;
import com.sprint.findex.sb02findexteam4.indexData.dto.IndexDataResponse;
import com.sprint.findex.sb02findexteam4.indexData.dto.IndexDataUpdateRequest;
import com.sprint.findex.sb02findexteam4.indexData.service.IndexDataService;
import com.sprint.findex.sb02findexteam4.indexInfo.entity.SourceType;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/index-data")
public class IndexDataController {

    private final IndexDataService indexDataService;

    public IndexDataController(IndexDataService indexDataService) {
        this.indexDataService = indexDataService;
    }

    @PostMapping
    public ResponseEntity<IndexDataResponse> createIndexData(@RequestBody IndexDataCreateRequest request) {
        try {
            IndexDataResponse createdIndexData = indexDataService.create(request, SourceType.USER);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdIndexData); //201 Created
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); //404 Not Found
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); //400 Bad Request
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); //500 Internal Sever Error
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<IndexDataResponse> updateIndexData(@PathVariable Long id,@RequestBody IndexDataUpdateRequest indexDataRequest) {
        try {
            IndexDataResponse updatedIndexData = indexDataService.update(id, indexDataRequest);
            return ResponseEntity.ok(updatedIndexData); //200 OK
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); //404 Not Found
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); //400 Bad Request
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); //500 Internal Server Error
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIndexData(@PathVariable long id) {
        try {
            indexDataService.delete(id);
            return ResponseEntity.noContent().build(); //204 No Content
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); //404 Not Found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); //500 Internal Server Error
        }
    }


}
