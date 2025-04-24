//package com.sprint.findex.sb02findexteam4.indexData.controller;
//
//import com.sprint.findex.sb02findexteam4.indexData.IndexData;
//import jakarta.persistence.EntityNotFoundException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/index-data")
//public class IndexDataController {
//
//    private final IndexDataService indexDataService;
//
//    public IndexDataController(IndexDataService indexDataService) {
//        this.indexDataService = indexDataService;
//    }
//
//    @PostMapping
//    public ResponseEntity<IndexData> createIndexData(@RequestBody IndexDataRequest IndexDataRequest) {
//        try {
//            IndexData createdIndexData = indexDataService.createIndexData(indexDataRequest);
//            return ResponseEntity.status(HttpStatus.CREATED).body(createdIndexData); //201 Created
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); //404 Not Found
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); //400 Bad Request
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); //500 Internal Sever Error
//        }
//    }
//
//    @PatchMapping("/{id}")
//    public ResponseEntity<IndexData> updateIndexData(@PathVariable Long id,@RequestBody IndexDataRequest indexDataRequest) {
//        try {
//            IndexData updatedIndexData = indexDataService.updateIndexData(id, indexDataRequest);
//            return ResponseEntity.ok(updatedIndexData); //200 OK
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); //404 Not Found
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); //400 Bad Request
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); //500 Internal Server Error
//        }
//
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<void> deleteIndexData(@PathVariable long id) {
//        try {
//            indexDataService.deleteIndexDataById(id);
//            return ResponseEntity.noContent().build(); //204 No Content
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); //404 Not Found
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); //500 Internal Server Error
//        }
//    }
//
//
//}
