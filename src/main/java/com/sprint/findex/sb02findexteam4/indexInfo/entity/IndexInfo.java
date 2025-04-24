package com.sprint.findex.sb02findexteam4.indexInfo.entity;

import com.sprint.findex.sb02findexteam4.indexInfo.dto.IndexInfoCreateRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.ZonedDateTime;
import java.util.List;
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
  private String indexClassification;

  @Column(name = "index_name", nullable = false)
  private String indexName;

  @Column(name = "employment_items_count", nullable = false)
  private Integer employedItemsCount;

  @Column(name = "base_point_in_time", nullable = false)
  private ZonedDateTime basePointInTime;

  @Column(name = "base_index", nullable = false)
  private Double baseIndex;

  @Enumerated(EnumType.STRING)
  @Column(name = "source_type", nullable = false)
  private SourceType sourceType;

  @Column(name = "favorite", nullable = false)
  private Boolean favorite;

  public static IndexInfo create(IndexInfoCreateRequest dto, SourceType sourceType) {
    IndexInfo indexInfo = new IndexInfo();
    indexInfo.updateFromDto(dto);
    indexInfo.sourceType = sourceType;
    return indexInfo;
  }

  public void updateFromDto(IndexInfoCreateRequest dto) {
    if (dto.indexClassification() != null && !dto.indexClassification().equals(this.indexClassification)) {
      this.indexClassification = dto.indexClassification();
    }
    if (dto.indexName() != null && !dto.indexName().equals(this.indexName)) {
      this.indexName = dto.indexName();
    }
    if (dto.employedItemsCount() != null && !dto.employedItemsCount().equals(this.employedItemsCount)) {
      this.employedItemsCount = dto.employedItemsCount();
    }
    if (dto.basePointInTime() != null && !dto.basePointInTime().equals(this.basePointInTime)) {
      this.basePointInTime = dto.basePointInTime();
    }
    if (dto.baseIndex() != null && !dto.baseIndex().equals(this.baseIndex)) {
      this.baseIndex = dto.baseIndex();
    }
    if (dto.favorite() != null && !dto.favorite().equals(this.favorite)) {
      this.favorite = dto.favorite();
    }
  }


}
