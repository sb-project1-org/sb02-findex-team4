package com.sprint.findex.sb02findexteam4.index.data.entity;


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
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "index_data",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"index_info_id", "base_date"})
    })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class IndexData {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "index_info_id", nullable = false)
  private IndexInfo indexInfo;

  @Column(name = "base_date", nullable = false)
  private LocalDate baseDate;

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
    if (dto.marketPrice() != null && !dto.marketPrice().equals(this.marketPrice)) {
      this.marketPrice = dto.marketPrice();
    }
    if (dto.closingPrice() != null && !dto.closingPrice().equals(this.closingPrice)) {
      this.closingPrice = dto.closingPrice();
    }
    if (dto.highPrice() != null && !dto.highPrice().equals(this.highPrice)) {
      this.highPrice = dto.highPrice();
    }
    if (dto.lowPrice() != null && !dto.lowPrice().equals(this.lowPrice)) {
      this.lowPrice = dto.lowPrice();
    }
    if (dto.versus() != null && !dto.versus().equals(this.versus)) {
      this.versus = dto.versus();
    }
    if (dto.fluctuationRate() != null && !dto.fluctuationRate().equals(this.fluctuationRate)) {
      this.fluctuationRate = dto.fluctuationRate();
    }
    if (dto.tradingQuantity() != null && !dto.tradingQuantity().equals(this.tradingQuantity)) {
      this.tradingQuantity = dto.tradingQuantity();
    }
    if (dto.tradingPrice() != null && !dto.tradingPrice().equals(this.tradingPrice)) {
      this.tradingPrice = dto.tradingPrice();
    }
    if (dto.marketTotalAmount() != null && !dto.marketTotalAmount()
        .equals(this.marketTotalAmount)) {
      this.marketTotalAmount = dto.marketTotalAmount();
    }
    return this;
  }


}
