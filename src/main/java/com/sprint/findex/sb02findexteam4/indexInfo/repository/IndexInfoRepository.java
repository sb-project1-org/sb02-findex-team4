package com.sprint.findex.sb02findexteam4.indexInfo.repository;

import com.sprint.findex.sb02findexteam4.indexInfo.entity.IndexInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexInfoRepository extends JpaRepository<IndexInfo, Long> {

  Page<IndexInfo> findByIndexClassificationName(String indexClassificationName, Pageable pageable);
  Page<IndexInfo> findByIndexName(String indexName, Pageable pageable);
  Page<IndexInfo> findByFavorite(boolean favorite, Pageable pageable);

  Page<IndexInfo> findByIndexClassificationNameAndIndexName(String type, String classificationName, Pageable pageable);
  Page<IndexInfo> findByIndexClassificationNameAndIndexNameAndFavorite(String type, String classificationName, boolean favorite, Pageable pageable);
}
