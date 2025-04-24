package com.sprint.findex.sb02findexteam4.sync.dto;

import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateRequest;
import java.util.List;

public record ApiRequest(
    List<IndexInfoCreateRequest> infoRequests,
    List<IndexDataFromApi> dataRequests
) {

}
