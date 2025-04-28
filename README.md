# Findex

- 가볍고 빠른 외부 API 연동 금융 분석 도구
- 금융 지수 데이터를 한눈에 제공하는 대시보드 서비스
- 프로젝트 기간: 2025.04.21 ~ 2025.04.29

(팀 협업 문서 링크 게시)

---

## 팀원 구성
김창환 (개인 Github 링크)   
박상혁 (https://github.com/manleKim)   
이영인 (https://github.com/ddday366)  
이종원 (https://github.com/BrotherMountain)  
최성현 (https://github.com/hakSick)

---

## 기술 스택

- Backend: Spring Boot, Spring Data JPA, Spring Scheduler, Lombok, MapStruct, Validation
- Database: PostgreSQL
- 공통 Tool: Git & Github, Discord, Notion
---


## **팀원별 구현 기능 상세**

### **김창환**

(자신이 개발한 기능에 대한 사진이나 gif 파일 첨부)

- **지수 정보 컨트롤러**
    - 등록, 수정, 삭제, 조회 API 매핑
- **지수 데이터 컨트롤러**
    - 등록, 수정, 삭제, 조회, Export API 매핑 및 요청 파라미터 처리
- **연동 작업 컨트롤러**
    - 수동 연동 실행 및 연동 작업 이력 조회 API 매핑
- **자동 연동 설정 컨트롤러**
    - 자동 연동 등록, 수정, 목록 조회 API 매핑

---

### **박상혁**

(자신이 개발한 기능에 대한 사진이나 gif 파일 첨부)

- **지수 정보 연동 API**
    - Open API를 응용하여 지수 정보 등록/수정
    - 사용자 직접 연동 기능 및 결과 기록
- **지수 데이터 연동 API**
    - `{지수}`, `{대상 날짜}` 조건으로 Open API를 통해 데이터 등록/수정
    - 사용자 직접 연동 기능 및 이력 관리
- **연동 작업 목록 조회 API**
    - `{유형}`, `{지수}`, `{대상 날짜}`, `{작업자}`, `{결과}`, `{작업일시}` 기준 검색 및 조회
    - `{대상 날짜}`, `{작업일시}` 정렬 및 페이지네이션

---

### **이영인**

(자신이 개발한 기능에 대한 사진이나 gif 파일 첨부)

- **지수 정보 등록 API**
    - `{지수 분류명}`, `{지수명}`, `{채용 종목 수}`, `{기준 시점}`, `{기준 지수}`, `{즐겨찾기}` 등록
    - `{지수 분류명}`, `{지수명}` 중복 방지 로직 구현
    - Open API 연동 자동 등록 처리
    - 등록 시 자동 연동 설정 초기화
- **지수 정보 수정 API**
    - `{채용 종목 수}`, `{기준 시점}`, `{기준 지수}`, `{즐겨찾기}` 수정 처리
    - Open API를 통한 자동 수정 기능 제공
- **지수 정보 삭제 API**
    - 지수 정보 삭제 시 관련 지수 데이터 동시 삭제 처리
- **지수 정보 목록 조회 API**
    - `{지수 분류명}`, `{지수명}`, `{즐겨찾기}` 검색 및 필터링 기능
    - `{지수 분류명}`, `{지수명}`, `{채용 종목 수}` 정렬, 페이지네이션 초안 기능 구현

---

### **이종원**

(자신이 개발한 기능에 대한 사진이나 gif 파일 첨부)

- **자동 연동 설정 등록 API**
  - 지수 등록 시 기본 비활성화 상태로 설정 저장
- **자동 연동 설정 수정 API**
  - `{활성화}` 값 변경 처리
- **자동 연동 설정 목록 조회 API**
  - `{지수}`, `{활성화}` 조건 검색 및 필터링, 정렬, 페이지네이션 기능
- **지수 데이터 연동 자동화 배치 프로세스**
  - Spring Scheduler를 이용한 1일 주기 데이터 연동
  - 활성화된 지수만 자동 연동 및 최신 데이터 반영
- **지수 정보 커서 기반 페이징 및 동적 검색 기능**
  - 커서 기반 페이징 방식으로 `IndexInfo` 데이터 조회 및 `size + 1`개 데이터 조회하여 `hasNext` 여부 판단
  - `{지수 분류명}`, `{지수명}`, `{즐겨찾기}` 조건으로 `IndexInfo`에 대한 동적 필터링 처리
  - 커서 및 `idAfter` 값을 사용하여 페이지네이션을 위한 커서 기반 조건 생성
  - 주어진 정렬 기준 및 필터 조건에 맞게 데이터를 조회하고, 커서 기반 페이지네이션 응답 구성
- **지수 정보 목록 조회 API**
  - `{지수 분류명}`, `{지수명}`, `{즐겨찾기}` 검색 및 필터링 기능
  - `{지수 분류명}`, `{지수명}`, `{채용 종목 수}` 정렬, 페이지네이션 기능 수정

---

### **최성현**

(자신이 개발한 기능에 대한 사진이나 gif 파일 첨부)

- **지수 데이터 등록 API**
    - `{지수}`, `{날짜}` 기준 등록
    - 중복 방지 및 Open API 연동 자동 등록 처리
- **지수 데이터 수정 API**
    - `{지수}`, `{날짜}` 제외 모든 속성 수정 처리
    - Open API를 활용한 자동 수정 기능 구현
- **지수 데이터 삭제 API**
    - 지수 데이터 단건 삭제 기능 구현
- **지수 데이터 목록 조회 API**
    - `{지수}`, `{날짜}` 조건 검색 및 필터링
    - 소스 타입 제외 속성 기준 정렬 및 페이지네이션 기능
- **지수 데이터 Export 기능**
    - 필터링/정렬된 데이터를 CSV 파일로 Export 기능 구현
- **대시보드 주요 기능**
    - **주요 지수 현황 요약**
        - 즐겨찾기 지수 성과 정보 표시 (종가 기준)
    - **지수 차트 기능**
        - 월/분기/년 단위 시계열 데이터
        - 5일, 20일 이동평균선 계산 및 표시
    - **지수 성과 분석 랭킹**
        - 전일/전주/전월 대비 종가 성과 랭킹 계산 및 제공

---

## 파일 구조

```yaml

src
┣ main
┃ ┣ java
┃ ┃ ┣ com
┃ ┃ ┃ ┣ sb02findexteam4
┃ ┃ ┃ ┃ ┣ config
┃ ┃ ┃ ┃ ┃ ┣ MessageConfig.java
┃ ┃ ┃ ┃ ┃ ┣ QueryDslConfig.java
┃ ┃ ┃ ┃ ┃ ┗ SchedulerConfig.java
┃ ┃ ┃ ┃ ┣ exception
┃ ┃ ┃ ┃ ┃ ┣ AlreadyExistsException.java
┃ ┃ ┃ ┃ ┃ ┣ ErrorCode.java
┃ ┃ ┃ ┃ ┃ ┣ ErrorResponse.java
┃ ┃ ┃ ┃ ┃ ┣ ExternalApiException.java
┃ ┃ ┃ ┃ ┃ ┣ GlobalExceptionHandler.java
┃ ┃ ┃ ┃ ┃ ┣ InvalidRequestException.java
┃ ┃ ┃ ┃ ┃ ┣ NormalExcepiton.java
┃ ┃ ┃ ┃ ┃ ┣ NotFoundException.java
┃ ┃ ┃ ┃ ┃ ┗ SystemException.java
┃ ┃ ┃ ┃ ┣ index
┃ ┃ ┃ ┃ ┃ ┣ data
┃ ┃ ┃ ┃ ┃ ┃ ┣ controller
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ IndexDataController.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ dto
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ ChartPoint.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ CursorPageResponseIndexDataDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ IndexChartDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ IndexDataCreateRequest.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ IndexDataCsvExportCommand.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ IndexDataDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ IndexDataFindCommand.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ IndexDataDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ IndexDataFindCommand.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ IndexDataResponse.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ IndexDataUpdateRequest.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ IndexPerformanceDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ RankedIndexPerfomanceDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ entity
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ IndexData.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ PeriodType.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ repository
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ IndexDataRepository.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ IndexDataRepositoryCustom.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ IndexDataRepositoryImpl.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ service
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ BasicIndexDataService.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ IndexDataService.java
┃ ┃ ┃ ┃ ┃ ┣ info
┃ ┃ ┃ ┃ ┃ ┃ ┣ controller
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ IndexInfoController.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ dto
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ CursorPageResponseIndexInfoDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ IndexInfoCreateCommand.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ IndexInfoCreateRequest.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ IndexInfoDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ IndexInfoSearchCondition.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ IndexInfoSearchRequest.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ IndexInfoSummaryDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ IndexInfoUpdateRequest.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ entity
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ IndexInfo.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ SourceType.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ repository
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ CustomIndexInfoRepository.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ CustomIndexInfoRepository.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ IndexInfoRepository.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ service
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ BasicIndexInfoService.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ BasicIndexInfoValidator.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ IndexInfoService.java
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ IndexDataValidator.java
┃ ┃ ┃ ┃ ┣ swagger
┃ ┃ ┃ ┃ ┃ ┣ AuthSyncConfigApi.java
┃ ┃ ┃ ┃ ┃ ┣ IndexInfoApi.java
┃ ┃ ┃ ┃ ┃ ┣ IndexDataApi.java
┃ ┃ ┃ ┃ ┃ ┗ SyncJobApi.java
┃ ┃ ┃ ┃ ┣ sync
┃ ┃ ┃ ┃ ┃ ┣ controller
┃ ┃ ┃ ┃ ┃ ┃ ┣ AutoSyncConfigController.java
┃ ┃ ┃ ┃ ┃ ┃ ┗ SYncJobController.java
┃ ┃ ┃ ┃ ┃ ┣ dto
┃ ┃ ┃ ┃ ┃ ┃ ┣ ApiRequest.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ AutoSyncConfigConditon.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ AutoSyncConfigDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ AutoSyncConfigFindCommand.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ AutoSyncConfigSearchRequest.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ AutoSyncConfigUpdateCommand.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ AutoSyncConfigUpdateRequest.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ Body.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ CursorPageResponseAutoSyncConfig.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ CursorPageResponseSyncJobDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ Header.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ IndexDataFromApi.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ IndexDataSyncRequest.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ Item.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ Items.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ MarketIndexResponse.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ Response.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ SyncJobHistoryCreateDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ SyncJobHistoryDto.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ SyncJobSearchConditon.java
┃ ┃ ┃ ┃ ┃ ┃ ┗ SyncJobSearchRequest.java
┃ ┃ ┃ ┃ ┃ ┣ entity
┃ ┃ ┃ ┃ ┃ ┃ ┣ AutoSyncConfig.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ JobResult.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ JobType.java
┃ ┃ ┃ ┃ ┃ ┃ ┗ SyncJobHistory.java
┃ ┃ ┃ ┃ ┃ ┣ mapper
┃ ┃ ┃ ┃ ┃ ┃ ┗ SyncJobHistoryMagger.java
┃ ┃ ┃ ┃ ┃ ┣ repository
┃ ┃ ┃ ┃ ┃ ┃ ┣ AutoSyncConfigRepository.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ CustomAutoSyncConfigRepository.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ CustomAutoSyncConfigRepository.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ CustomSyncJobHistoryRepository.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ CustomSyncJobHistoryRepositoryImpl.java
┃ ┃ ┃ ┃ ┃ ┃ ┗ SyncJobHistoryRepository.java
┃ ┃ ┃ ┃ ┃ ┣ service
┃ ┃ ┃ ┃ ┃ ┃ ┣ AutoSyncConfigFIndUseCase.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ AutoSyncConfigService.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ BasicAutoSyncConfigService.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ BasicSyncJobHistoryService.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ BasicSyncJobServicejava
┃ ┃ ┃ ┃ ┃ ┃ ┣ ScheduledTasks.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ ScheduledApiUseCase.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ ScheduledHistoryService.java
┃ ┃ ┃ ┃ ┃ ┃ ┣ ScheduledService.java
┃ ┃ ┃ ┃ ┃ ┃ ┗ ScheduledUserUseCase.java
┃ ┃ ┃ ┃ ┣ util
┃ ┃ ┃ ┃ ┃ ┣ CustomQuerydslSortUtils.java
┃ ┃ ┃ ┃ ┃ ┣ IpUtils.java
┃ ┃ ┃ ┃ ┃ ┗ TimeUtils.java
┃ ┃ ┃ ┃ ┣ Sb02FindexTeam4Application.java
┃ ┣ resource
┃ ┃ ┣ static
┃ ┃ ┃ ┣ assets
┃ ┃ ┃ ┃ ┣ Index-CNh_iLGU.js
┃ ┃ ┃ ┃ ┗ Index-Dtn62Xmo.css
┃ ┃ ┃ ┣ favicon.ico
┃ ┃ ┃ ┗ index.html
┃ ┃ ┣ application.yaml
┃ ┃ ┣ messages.properties
┣ build.gradle
┣ docker-compose.yaml
┣ .gitignore
┣ schema.sql
┗ README.md
```

---

## **구현 홈페이지(추후에 완성할 예정)**

(개발한 홈페이지에 대한 링크 게시)

---

## **프로젝트 회고록(추후에 완성할 예정)**

(제작한 발표자료 링크 혹은 첨부파일 첨부)