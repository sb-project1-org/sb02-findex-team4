package com.sprint.findex.sb02findexteam4.index.data;


import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataCreateRequest;
import com.sprint.findex.sb02findexteam4.index.data.dto.IndexDataUpdateRequest;
import com.sprint.findex.sb02findexteam4.index.info.entity.IndexInfo;
import com.sprint.findex.sb02findexteam4.index.info.entity.SourceType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
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

  @ManyToOne
  @JoinColumn(name = "index_info_id", nullable = false)
  private IndexInfo indexInfo;

  @Column(name = "base_date", nullable = false)
  private Instant baseDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "source_type", nullable = false)
  private SourceType sourceType;

  @Column(name = "market_price", precision = 15, scale = 4)
  private BigDecimal marketPrice;

  @Column(name = "closing_price", precision = 15, scale = 4)
  private BigDecimal closingPrice;

  @Column(name = "high_price", precision = 15, scale = 4)
  private BigDecimal highPrice;

  @Column(name = "low_price", precision = 15, scale = 4)
  private BigDecimal lowPrice;

  @Column(name = "versus", precision = 15, scale = 4)
  private BigDecimal versus;

  @Column(name = "fluctuation_rate", precision = 15, scale = 4)
  private BigDecimal fluctuationRate;

  @Column(name = "trading_quantity")
  private Long tradingQuantity;

  @Column(name = "trading_price")
  private Long tradingPrice;

  @Column(name = "market_total_amount")
  private Long marketTotalAmount;

  public IndexData update(IndexDataUpdateRequest dto) {
    this.marketPrice = dto.marketPrice();
    this.closingPrice = dto.closingPrice();
    this.highPrice = dto.highPrice();
    this.lowPrice = dto.lowPrice();
    this.versus = dto.versus();
    this.fluctuationRate = dto.fluctuationRate();
    this.tradingQuantity = dto.tradingQuantity();
    this.tradingPrice = dto.tradingPrice();
    this.marketTotalAmount = dto.marketTotalAmount();
    return this;
  }

  public static IndexData of(IndexDataCreateRequest req, Instant instant, IndexInfo indexInfo, SourceType sourceType) {
    IndexData data = new IndexData();
    data.indexInfo = indexInfo;
    data.baseDate = instant;
    data.sourceType = sourceType;
    data.marketPrice = req.marketPrice();
    data.closingPrice = req.closingPrice();
    data.highPrice = req.highPrice();
    data.lowPrice = req.lowPrice();
    data.versus = req.versus();
    data.fluctuationRate = req.fluctuationRate();
    data.tradingQuantity = req.tradingQuantity();
    data.tradingPrice = req.tradingPrice();
    data.marketTotalAmount = req.marketTotalAmount();
    return data;
  }
}
