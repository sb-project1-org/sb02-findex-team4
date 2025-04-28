package com.sprint.findex.sb02findexteam4.swagger;

import com.sprint.findex.sb02findexteam4.index.data.dto.CursorPageResponseIndexDataDto;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexChartDto;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataCreateRequest;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataResponse;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataUpdateRequest;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexPerformanceDto;
import com.sprint.findex.sb02findexteam4.index.data.dto.RankedIndexPerformanceDto;
import com.sprint.findex.sb02findexteam4.index.data.entity.PeriodType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "지수 데이터 목록 조회", description = "지수 데이터 관리 API")
public interface IndexDataApi {

  public interface IndexData {

    @Operation(summary = "지수 데이터 목록 조회", description = "지수 데이터 목록을 조회합니다. 필터링, 정렬, 커서 기반 페이지네이션을 지원합니다.", responses = {
        @ApiResponse(responseCode = "200", description = "지수 데이터 목록 조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청(유효하지 않은 필터 값 등)"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    ResponseEntity<CursorPageResponseIndexDataDto> getIndexDataList(
        @RequestParam(required = false) Long indexInfoId,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate,
        @RequestParam(required = false) Long idAfter,
        @RequestParam(required = false) String cursor,
        @RequestParam(defaultValue = "baseDate") String sortField,
        @RequestParam(defaultValue = "desc") String sortDirection,
        @RequestParam(defaultValue = "10") int size);

    @Operation(summary = "지수 데이터 등록", description = "새로운 지수 데이터를 등록합니다.", responses = {
        @ApiResponse(responseCode = "201", description = "지수 데이터 생성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청(유효하지 않은 데이터 값 등)"),
        @ApiResponse(responseCode = "404", description = "참조하는 지수 정보를 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")})
    ResponseEntity<IndexDataResponse> createIndexData(
        @RequestBody IndexDataCreateRequest request);

    @Operation(summary = "지수 데이터 삭제", description = "지수 데이터를 삭제합니다.", responses = {
        @ApiResponse(responseCode = "204", description = "지수 데이터 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "삭제할 지수 데이터를 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")})
    ResponseEntity<Void> deleteIndexData(@PathVariable long id);

    @Operation(summary = "지수 데이터 수정", description = "기존 지수 데이터를 수정합니다.", responses = {
        @ApiResponse(responseCode = "200", description = "지수 데이터 수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청(유효하지 않은 데이터 값 등)"),
        @ApiResponse(responseCode = "404", description = "수정할 지수 데이터를 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")})
    ResponseEntity<IndexDataResponse> updateIndexData(@PathVariable Long id,
        @RequestBody IndexDataUpdateRequest indexDataRequest);

    @Operation(summary = "지수 차트 조회", description = "지수의 차트 데이터를 조회합니다.", responses = {
        @ApiResponse(responseCode = "200", description = "차트 데이터 수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청(유효하지 않은 기간 유형 등)"),
        @ApiResponse(responseCode = "404", description = "지수 정보를 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")})
    ResponseEntity<IndexChartDto> getChartData(
        @PathVariable("id") Long indexInfoId,
        @RequestParam(value = "periodType", defaultValue = "DAILY") PeriodType periodType);

    @Operation(summary = "지수 성과 랭킹 조회", description = "지수의 성과 분석 랭킹을 조회합니다.", responses = {
        @ApiResponse(responseCode = "200", description = "성과 랭킹 조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청(유효하지 않은 기간 유형 등)"),
        @ApiResponse(responseCode = "500", description = "서버 오류")})
    ResponseEntity<List<RankedIndexPerformanceDto>> getPerformanceRank(
        @RequestParam(required = false) Long indexInfoId,
        @RequestParam(defaultValue = "DAILY") PeriodType periodType,
        @RequestParam(defaultValue = "10") int limit
    );

    @Operation(summary = "관심 지수 성과 조회", description = "즐겨찾기로 등록된 지수들의 성과를 조회합니다.", responses = {
        @ApiResponse(responseCode = "200", description = "관심 지수 성과 조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 오류")})
    ResponseEntity<List<IndexPerformanceDto>> getFavoriteIndexPerformances(
        @RequestParam(defaultValue = "DAILY") PeriodType periodType);

    @Operation(summary = "지수 데이터 CSV export", description = "지수 데이터를 CSV 파일로 export합니다.", responses = {
        @ApiResponse(responseCode = "200", description = "CSV 파일 생성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청(유효하지 않은 필터 값 등"),
        @ApiResponse(responseCode = "500", description = "서버 오류")})
    ResponseEntity<byte[]> exportCsv(
        @RequestParam(required = false) Long indexInfoId,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate,
        @RequestParam(defaultValue = "baseDate") String sortField,
        @RequestParam(defaultValue = "desc") String sortDirection
    );
  }
}
