package com.sprint.findex.sb02findexteam4.indexInfo.entity;

import com.sprint.findex.sb02findexteam4.indexInfo.dto.IndexInfoCreateRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "index_info",
    uniqueConstraints = @UniqueConstraint(columnNames = {"index_classification", "index_name"})
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IndexInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "index_classification", nullable = false)
  private String indexClassificationName;

  @Column(name = "index_name", nullable = false)
  private String indexName;

  @Column(name = "employment_items_count", nullable = false)
  private Integer employedItemsCount;

  @Column(name = "base_point_in_time", nullable = false)
  private Instant basePointInTime;

  @Column(name = "base_index", nullable = false)
  private Double baseIndex;

  @Enumerated(EnumType.STRING)
  @Column(name = "source_type", columnDefinition = "source_type", nullable = false)
  private SourceType sourceType;

  @Column(name = "favorite", nullable = false)
  private Boolean favorite;

  public static IndexInfo create(IndexInfoCreateRequestDto dto) {
    IndexInfo indexInfo = new IndexInfo();
    indexInfo.updateFromDto(dto);
    indexInfo.sourceType = SourceType.USER;
    return indexInfo;
  }

  public void updateFromDto(IndexInfoCreateRequestDto dto) {
    this.indexClassificationName = dto.indexClassificationName();
    this.indexName = dto.indexName();
    this.employedItemsCount = dto.employedItemsCount();
    this.basePointInTime = dto.basePointInTime();
    this.baseIndex = dto.baseIndex();
    this.favorite = dto.favorite();
  }
}
