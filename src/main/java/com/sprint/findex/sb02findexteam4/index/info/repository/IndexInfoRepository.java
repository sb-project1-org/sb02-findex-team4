package com.sprint.findex.sb02findexteam4.index.info.repository;

import com.sprint.findex.sb02findexteam4.index.info.entity.IndexInfo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexInfoRepository extends JpaRepository<IndexInfo, Long> {

  List<IndexInfo> findAllByFavoriteTrue();

  Page<IndexInfo> findByIndexClassification(String indexClassification, Pageable pageable);

  Page<IndexInfo> findByIndexName(String indexName, Pageable pageable);

  Page<IndexInfo> findByFavorite(boolean favorite, Pageable pageable);

//  Page<IndexInfo> findByIndexClassificationAndIndexName(String type, String classification,
//      Pageable pageable);

  Optional<IndexInfo> findByIndexClassificationAndIndexName(String indexClassification,
      String indexName);

  Page<IndexInfo> findByIndexClassificationAndIndexNameAndFavorite(String type,
      String classification, boolean favorite, Pageable pageable);

  boolean existsByIndexName(String indexName);
}
