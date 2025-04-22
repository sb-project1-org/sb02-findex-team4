package com.sprint.findex.sb02findexteam4.indexData;


import com.sprint.findex.sb02findexteam4.indexInfo.IndexInfo;
import com.sprint.findex.sb02findexteam4.indexInfo.SourceType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "index_data",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"index_info_id", "base_date"})
    })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class IndexData {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "index_info_id", nullable = false)
  private IndexInfo indexInfo;

  @Column(name = "base_date", nullable = false)
  private Instant baseDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "source_type", columnDefinition = "source_type", nullable = false)
  private SourceType sourceType;

  @Column(name = "market_price", precision = 15, scale = 4)
  private Double marketPrice;

  @Column(name = "closing_price", precision = 15, scale = 4)
  private Double closingPrice;

  @Column(name = "high_price", precision = 15, scale = 4)
  private Double highPrice;

  @Column(name = "low_price", precision = 15, scale = 4)
  private Double lowPrice;

  @Column(name = "versus", precision = 15, scale = 4)
  private Double versus;

  @Column(name = "fluctuation_rate", precision = 15, scale = 4)
  private Double fluctuationRate;

  @Column(name = "trading_quantity")
  private Long tradingQuantity;

  @Column(name = "trading_price")
  private Long tradingPrice;

  @Column(name = "market_total_amount")
  private Long marketTotalAmount;
}
