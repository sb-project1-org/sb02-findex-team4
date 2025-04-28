package com.sprint.findex.sb02findexteam4.index.info.entity;

import static com.sprint.findex.sb02findexteam4.exception.ErrorCode.INDEX_INFO_BAD_REQUEST;

import com.sprint.findex.sb02findexteam4.exception.NormalException;
import com.sprint.findex.sb02findexteam4.index.data.entity.IndexData;
import com.sprint.findex.sb02findexteam4.index.info.dto.IndexInfoCreateCommand;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "index_info",
    uniqueConstraints = @UniqueConstraint(columnNames = {"index_classification", "index_name"})
)
@Getter
@Setter
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
  private Instant basePointInTime;

  @Column(name = "base_index", nullable = false)
  private Double baseIndex;

  @Enumerated(EnumType.STRING)
  @Column(name = "source_type", nullable = false)
  private SourceType sourceType;

  @Column(name = "favorite", nullable = false)
  private Boolean favorite;

  @OneToMany(mappedBy = "indexInfo", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<IndexData> indexDataList;

  public static IndexInfo create(IndexInfoCreateCommand dto) {
    Validator.validate(dto);
    IndexInfo indexInfo = new IndexInfo();
    indexInfo.updateFromDto(dto);
    indexInfo.sourceType = dto.sourceType();
    return indexInfo;
  }

  public void updateFromDto(IndexInfoCreateCommand dto) {
    if (dto.indexClassification() != null && !dto.indexClassification()
        .equals(this.indexClassification)) {
      this.indexClassification = dto.indexClassification();
    }
    if (dto.indexName() != null && !dto.indexName().equals(this.indexName)) {
      this.indexName = dto.indexName();
    }
    if (dto.employedItemsCount() != null && !dto.employedItemsCount()
        .equals(this.employedItemsCount)) {
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

  public static class Validator {

    public static void validate(IndexInfoCreateCommand dto) {
      if (dto.indexClassification() == null || dto.indexClassification().isBlank()) {
        throw new NormalException(INDEX_INFO_BAD_REQUEST);
      }

      if (dto.indexName() == null || dto.indexName().isBlank()) {
        throw new NormalException(INDEX_INFO_BAD_REQUEST);
      }

      if (dto.employedItemsCount() == null || dto.employedItemsCount() < 0) {
        throw new NormalException(INDEX_INFO_BAD_REQUEST);
      }

      if (dto.basePointInTime() == null) {
        throw new NormalException(INDEX_INFO_BAD_REQUEST);
      }

      if (dto.baseIndex() == null || dto.baseIndex() < 0) {
        throw new NormalException(INDEX_INFO_BAD_REQUEST);
      }

      if (dto.favorite() == null) {
        throw new NormalException(INDEX_INFO_BAD_REQUEST);
      }
    }
  }
}
