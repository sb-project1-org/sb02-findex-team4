package com.sprint.findex.sb02findexteam4.index.info.dto;

import lombok.Builder;

@Builder
public record IndexInfoSearchCondition(
    String indexClassification,
    String indexName,
    Boolean favorite,
    Long idAfter,
    String cursor,
    String sortField,
    String sortDirection,
    int size
) {

  public static IndexInfoSearchCondition of(
      IndexInfoSearchRequest request,
      Boolean favoriteCond,
      String sortField,
      String sortDirection,
      int size) {
    return IndexInfoSearchCondition.builder()
        .indexClassification(request.indexClassification)
        .indexName(request.indexName)
        .favorite(favoriteCond)
        .idAfter(request.idAfter)
        .cursor(request.cursor)
        .sortField(sortField)
        .sortDirection(sortDirection)
        .size(size)
        .build();
  }

}