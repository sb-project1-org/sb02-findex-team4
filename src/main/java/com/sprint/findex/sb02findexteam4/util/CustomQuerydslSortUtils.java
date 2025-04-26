package com.sprint.findex.sb02findexteam4.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.sprint.findex.sb02findexteam4.index.info.entity.QIndexInfo;
import com.sprint.findex.sb02findexteam4.sync.entity.QAutoSyncConfig;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class CustomQuerydslSortUtils {

  public static OrderSpecifier<?>[] createOrderSpecifiersForIndexInfo(QIndexInfo indexInfo,
      Pageable pageable) {
    List<OrderSpecifier<?>> orders = new ArrayList<>();

    for (Sort.Order sortOrder : pageable.getSort()) {
      Order direction = sortOrder.isAscending() ? Order.ASC : Order.DESC;
      String property = sortOrder.getProperty();

      OrderSpecifier<?> orderSpecifier = switch (property) {
        case "indexClassification" ->
            new OrderSpecifier<>(direction, indexInfo.indexClassification);
        case "indexName" -> new OrderSpecifier<>(direction, indexInfo.indexName);
        case "employedItemsCount" -> new OrderSpecifier<>(direction, indexInfo.employedItemsCount);
        default -> null; // 알 수 없는 필드는 무시
      };

      if (orderSpecifier != null) {
        orders.add(orderSpecifier);
      }
    }

    return orders.toArray(new OrderSpecifier[0]);
  }

  public static OrderSpecifier<?>[] createOrderSpecifiersForAutoSyncConfig(
      QAutoSyncConfig autoSyncConfig, Pageable pageable) {
    List<OrderSpecifier<?>> orders = new ArrayList<>();

    for (Sort.Order sortOrder : pageable.getSort()) {
      Order direction = sortOrder.isAscending() ? Order.ASC : Order.DESC;
      String property = sortOrder.getProperty();

      OrderSpecifier<?> orderSpecifier = switch (property) {
        case "indexInfo.indexName" -> new OrderSpecifier<>(direction, autoSyncConfig.indexInfo.id);
        case "enabled" -> new OrderSpecifier<>(direction, autoSyncConfig.enabled);
        default -> null; // 알 수 없는 필드는 무시
      };

      if (orderSpecifier != null) {
        orders.add(orderSpecifier);
      }
    }

    return orders.toArray(new OrderSpecifier[0]);
  }

  public static OrderSpecifier<?>[] createOrderSpecifiers(QIndexInfo indexInfo, Pageable pageable) {
    List<OrderSpecifier<?>> orders = new ArrayList<>();

    for (Sort.Order sortOrder : pageable.getSort()) {
      Order direction = sortOrder.isAscending() ? Order.ASC : Order.DESC;
      String property = sortOrder.getProperty();

      OrderSpecifier<?> orderSpecifier = switch (property) {
        case "indexClassification" ->
            new OrderSpecifier<>(direction, indexInfo.indexClassification);
        case "indexName" -> new OrderSpecifier<>(direction, indexInfo.indexName);
        case "employedItemsCount" -> new OrderSpecifier<>(direction, indexInfo.employedItemsCount);
        default -> null; // 알 수 없는 필드는 무시
      };

      if (orderSpecifier != null) {
        orders.add(orderSpecifier);
      }
    }

    return orders.toArray(new OrderSpecifier[0]);
  }


}
