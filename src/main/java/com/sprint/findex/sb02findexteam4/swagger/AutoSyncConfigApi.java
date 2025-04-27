package com.sprint.findex.sb02findexteam4.swagger;

import com.sprint.findex.sb02findexteam4.exception.ErrorResponse;
import com.sprint.findex.sb02findexteam4.sync.dto.AutoSyncConfigDto;
import com.sprint.findex.sb02findexteam4.sync.dto.AutoSyncConfigSearchRequest;
import com.sprint.findex.sb02findexteam4.sync.dto.AutoSyncConfigUpdateRequest;
import com.sprint.findex.sb02findexteam4.sync.dto.CursorPageResponseAutoSyncConfigDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "자동 연동 설정 관리", description = "자동 연동 설정 관리 API")
public interface AutoSyncConfigApi {

  @Operation(summary = "자동 연동 설정 수정", description =
      "기존 자동 연동 설정을 수정합니다.", responses = {
      @ApiResponse(responseCode = "200", description = "자동 연동 설정 수정 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청(유효하지 않은 설정값)", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "수정할 자동 연동 설정을 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<AutoSyncConfigDto> update(
      @Schema(description = "자동 연동 설정 ID") @PathVariable Long id, @RequestBody
      AutoSyncConfigUpdateRequest request);

  @Operation(summary = "자동 연동 설정 목록 조회", description =
      "자동 연동 설정 목록을 조회합니다. 필터링, 정렬, 커서 기반 페이지네이션을 지원합니다.", responses = {
      @ApiResponse(responseCode = "200", description = "자동 연동 설정 목록 조회 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청(유효하지 않은 설정값)", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<CursorPageResponseAutoSyncConfigDto> findAll(
      @ParameterObject
      @ModelAttribute AutoSyncConfigSearchRequest request,
      @Schema(description = "정렬 필드")
      @RequestParam(value = "sortField", required = false, defaultValue = "indexInfo.indexName") String sortField,
      @Schema(description = "정렬 방향")
      @RequestParam(value = "sortDirection", required = false, defaultValue = "asc") String sortDirection,
      @Schema(description = "페이지 크기")
      @RequestParam(value = "size", required = false, defaultValue = "10") int size,
      HttpServletRequest httpRequest
  );
}
