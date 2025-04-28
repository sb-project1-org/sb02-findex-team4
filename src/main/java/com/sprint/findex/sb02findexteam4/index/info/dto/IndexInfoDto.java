package com.sprint.findex.sb02findexteam4.index.info.dto;

import com.sprint.findex.sb02findexteam4.index.info.entity.IndexInfo;
import com.sprint.findex.sb02findexteam4.index.info.entity.SourceType;
import com.sprint.findex.sb02findexteam4.util.TimeUtils;
import lombok.Builder;

@Builder
public record IndexInfoDto(
    Long id,
    String indexClassification,
    String indexName,
    Integer employedItemsCount,
    String basePointInTime,
    Double baseIndex,
    SourceType sourceType,
    Boolean favorite
) {

    public static IndexInfoDto of(IndexInfo indexInfo) {
        return IndexInfoDto.builder()
            .id(indexInfo.getId())
            .indexClassification(indexInfo.getIndexClassification())
            .indexName(indexInfo.getIndexName())
            .employedItemsCount(indexInfo.getEmployedItemsCount())
            .basePointInTime(TimeUtils.formatedTimeString(indexInfo.getBasePointInTime()))
            .baseIndex(indexInfo.getBaseIndex())
            .sourceType(indexInfo.getSourceType())
            .favorite(indexInfo.getFavorite())
            .build();
    }
}