package com.sprint.findex.sb02findexteam4.sync.controller;

import com.sprint.findex.sb02findexteam4.swagger.AutoSyncConfigApi;
import com.sprint.findex.sb02findexteam4.sync.dto.search.AutoSyncConfigCondition;
import com.sprint.findex.sb02findexteam4.sync.dto.AutoSyncConfigDto;
import com.sprint.findex.sb02findexteam4.sync.dto.search.AutoSyncConfigSearchRequest;
import com.sprint.findex.sb02findexteam4.sync.dto.AutoSyncConfigUpdateCommand;
import com.sprint.findex.sb02findexteam4.sync.dto.AutoSyncConfigUpdateRequest;
import com.sprint.findex.sb02findexteam4.sync.dto.CursorPageResponseAutoSyncConfigDto;
import com.sprint.findex.sb02findexteam4.sync.service.AutoSyncConfigService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auto-sync-configs")
@RequiredArgsConstructor
public class AutoSyncConfigController implements AutoSyncConfigApi {

  private final AutoSyncConfigService autoSyncConfigService;

  @PatchMapping("/{id}")
  public ResponseEntity<AutoSyncConfigDto> update(@PathVariable Long id, @RequestBody
  AutoSyncConfigUpdateRequest request) {
    AutoSyncConfigUpdateCommand command = new AutoSyncConfigUpdateCommand(id,
        request.enabled());
    log.info("AutoSyncConfig Update <Controller> -> id {}, enabled {}", id, request.enabled());
    AutoSyncConfigDto result = autoSyncConfigService.update(command);

    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  @GetMapping
  public ResponseEntity<CursorPageResponseAutoSyncConfigDto> findAll(
      @ModelAttribute AutoSyncConfigSearchRequest request,
      @RequestParam(value = "sortField", required = false, defaultValue = "indexInfo.indexName") String sortField,
      @RequestParam(value = "sortDirection", required = false, defaultValue = "asc") String sortDirection,
      @RequestParam(value = "size", required = false, defaultValue = "10") int size,
      HttpServletRequest httpRequest
  ) {
    log.info(
        "AutoSyncConfig find <Controller> -> IndexInfoId {}, enabled {}, idAfter {}, cursor {}",
        request.getIndexInfoId(), request.isEnabled(), request.getIdAfter(), request.getCursor());
    boolean enabledProvided = httpRequest.getParameterMap().containsKey("enabled");

    Boolean enabledCond = enabledProvided ? request.isEnabled() : null;

    AutoSyncConfigCondition condition = AutoSyncConfigCondition.from(request, enabledCond,
        sortField, sortDirection, size);
    CursorPageResponseAutoSyncConfigDto response = autoSyncConfigService.findAll(
        condition);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
