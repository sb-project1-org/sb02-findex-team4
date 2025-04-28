package com.sprint.findex.sb02findexteam4.swagger;

import com.sprint.findex.sb02findexteam4.exception.ErrorResponse;
import com.sprint.findex.sb02findexteam4.sync.dto.CursorPageResponseSyncJobDto;
import com.sprint.findex.sb02findexteam4.sync.dto.IndexDataSyncRequest;
import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobHistoryDto;
import com.sprint.findex.sb02findexteam4.sync.dto.search.SyncJobSearchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "연동 작업 API", description = "연동 작업 관리 API")
@RequestMapping("/api/sync-jobs")
public interface SyncJobApi {

  @Operation(
      summary = "지수 정보 연동",
      description = "Open API를 통해 지수 정보를 연동합니다.",
      responses = {
          @ApiResponse(responseCode = "202", description = "연동 작업 생성 성공",
              content = @Content(schema = @Schema(implementation = SyncJobHistoryDto.class))),
          @ApiResponse(responseCode = "400", description = "잘못된 요청",
              content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(responseCode = "500", description = "서버 오류",
              content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      }
  )
  @PostMapping("/index-infos")
  ResponseEntity<List<SyncJobHistoryDto>> syncIndexInfos(HttpServletRequest request);

  @Operation(
      summary = "지수 데이터 연동",
      description = "Open API를 통해 지수 데이터를 연동합니다.",
      requestBody = @RequestBody(
          description = "지수 데이터 연동 요청",
          required = true,
          content = @Content(
              schema = @Schema(implementation = IndexDataSyncRequest.class)
          )
      ),
      responses = {
          @ApiResponse(responseCode = "202", description = "연동 작업 생성 성공",
              content = @Content(schema = @Schema(implementation = SyncJobHistoryDto.class))),
          @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효하지 않은 날짜 범위 등)",
              content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(responseCode = "404", description = "지수 정보를 찾을 수 없음",
              content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(responseCode = "500", description = "서버 오류",
              content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      }
  )
  @PostMapping("/index-data")
  ResponseEntity<List<SyncJobHistoryDto>> syncIndexData(
      @RequestBody(description = "지수 데이터 연동 요청 DTO") IndexDataSyncRequest indexDataSyncRequest,
      HttpServletRequest request
  );

  @Operation(
      summary = "연동 작업 목록 조회",
      description = "연동 작업 목록을 조회합니다. 필터링, 정렬, 커서 기반 페이지네이션을 지원합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "연동 작업 목록 조회 성공",
              content = @Content(schema = @Schema(implementation = CursorPageResponseSyncJobDto.class))),
          @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효하지 않은 필터 값 등)",
              content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(responseCode = "500", description = "서버 오류",
              content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      }
  )
  @GetMapping
  ResponseEntity<CursorPageResponseSyncJobDto> getSyncJobHistory(
      @ParameterObject
      @ModelAttribute SyncJobSearchRequest request,
      @Parameter(description = "정렬 필드", example = "jobTime")
      @RequestParam(defaultValue = "jobTime", required = false) String sortField,
      @Parameter(description = "정렬 방향 (asc 또는 desc)", example = "desc")
      @RequestParam(defaultValue = "desc", required = false) String sortDirection,
      @Parameter(description = "페이지 크기", example = "10")
      @RequestParam(defaultValue = "10", required = false) int size
  );
}