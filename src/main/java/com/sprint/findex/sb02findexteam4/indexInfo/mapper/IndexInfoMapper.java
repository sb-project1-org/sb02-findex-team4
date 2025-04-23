package com.sprint.findex.sb02findexteam4.indexInfo.mapper;

import com.sprint.findex.sb02findexteam4.indexInfo.dto.IndexInfoDto;
import com.sprint.findex.sb02findexteam4.indexInfo.entity.IndexInfo;
import org.springframework.stereotype.Component;

@Component
public class IndexInfoMapper {

  public IndexInfoDto toDto(IndexInfo entity) {
    return new IndexInfoDto(
        entity.getId(),
        entity.getIndexClassificationName(),
        entity.getIndexName(),
        entity.getEmployedItemsCount(),
        entity.getBasePointInTime(),
        entity.getBaseIndex(),
        entity.getSourceType(),
        entity.getFavorite()
    );
  }
}
