package com.sprint.findex.sb02findexteam4.swagger;

import com.sprint.findex.sb02findexteam4.sync.dto.IndexDataSyncRequest;
import com.sprint.findex.sb02findexteam4.sync.dto.SyncJobHistoryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "연동 작업 API", description = "연동 작업 관리 API")
public interface SyncJobApi {
    @Operation(summary = "지수 정보 연동",
        description = "Open API를 통해 지수 정보를 연동합니다.",
        responses = {
            @ApiResponse(responseCode = "202", description = "연동 작업 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오"),
        })
    ResponseEntity<List<SyncJobHistoryDto>> syncIndexInfos();

    @Operation(summary = "지수 데이터 연동",
        description = "Open API를 통해 지수 데이터를 연동합니다.",
        responses = {
            @ApiResponse(responseCode = "202", description = "연동 작업 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청(유효하지 않은 날짜 범위 등)"),
            @ApiResponse(responseCode = "404", description = "지수 정보를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류"),
        })
    ResponseEntity<List<SyncJobHistoryDto>> syncIndexData(IndexDataSyncRequest request);


}
