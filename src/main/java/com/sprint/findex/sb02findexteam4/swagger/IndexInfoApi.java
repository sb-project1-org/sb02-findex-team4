package com.sprint.findex.sb02findexteam4.swagger;

import com.sprint.findex.sb02findexteam4.index.info.dto.CursorPageResponseIndexInfoDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateRequest;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoSearchRequest;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoSummaryDto;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Index-Info-Controller", description = "IndexInfo 관련 API")
public interface IndexInfoApi {

  @Operation(summary = "지수 정보 등록", description = "새로운 지수 정보를 등록합니다.", responses = {
      @ApiResponse(responseCode = "201", description = "지수 정보 생성 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  ResponseEntity<IndexInfoDto> createIndexInfo(
      @RequestBody IndexInfoCreateRequest request
  );

  @Operation(summary = "지수 정보 조회", description = "ID로 지수 정보를 조회합니다.", responses = {
      @ApiResponse(responseCode = "200", description = "지수 정보 조회 성공"),
      @ApiResponse(responseCode = "404", description = "지수 정보를 찾을 수 없음"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  ResponseEntity<IndexInfoDto> findById(
      @PathVariable Long id
  );

  @Operation(summary = "지수 정보 요약 목록 조회", description = "지수 ID, 분류, 이름만 포함한 전체 지수 목록을 조회합니다.", responses = {
      @ApiResponse(responseCode = "200", description = "지수 정보 요약 목록 조회 성공"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  ResponseEntity<List<IndexInfoSummaryDto>> getIndexInfoSummaries();

  @Operation(summary = "지수 정보 목록 조회", description = "필터링, 정렬, 커서 기반 페이지네이션을 지원하여 지수 정보를 조회합니다.", responses = {
      @ApiResponse(responseCode = "200", description = "지수 정보 목록 조회 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  ResponseEntity<CursorPageResponseIndexInfoDto> getIndexInfoWithFilters(
      @ModelAttribute IndexInfoSearchRequest request,
      @RequestParam(defaultValue = "indexClassification", required = false) String sortField,
      @RequestParam(defaultValue = "asc", required = false) String sortDirection,
      @RequestParam(defaultValue = "10", required = false) int size,
      HttpServletRequest httpRequest
  );

  @Operation(summary = "지수 정보 수정", description = "기존 지수 정보를 수정합니다.", responses = {
      @ApiResponse(responseCode = "200", description = "지수 정보 수정 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청"),
      @ApiResponse(responseCode = "404", description = "지수 정보를 찾을 수 없음"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  ResponseEntity<IndexInfoDto> updateIndexInfo(
      @PathVariable Long id,
      @RequestBody IndexInfoUpdateRequest request
  );

  @Operation(summary = "지수 정보 삭제", description = "지수 정보를 삭제합니다.", responses = {
      @ApiResponse(responseCode = "204", description = "지수 정보 삭제 성공"),
      @ApiResponse(responseCode = "404", description = "지수 정보를 찾을 수 없음"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  ResponseEntity<Void> delete(
      @PathVariable long id
  );
}
