package com.sprint.findex.sb02findexteam4.sync.entity;

import com.sprint.findex.sb02findexteam4.index.info.entity.IndexInfo;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@Table(name = "sync_job_history")
@Entity
public class SyncJobHistory {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "job_type", nullable = false)
  private JobType jobType;


  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "index_info_id")
  private IndexInfo indexInfo;

  @Timestamp
  @Column(name = "target_date")
  private Instant targetDate;

  @Column(name = "worker", nullable = false)
  private String worker;

  @Timestamp
  @Column(name = "job_time", nullable = false)
  private Instant jobTime;

  @Enumerated(EnumType.STRING)
  @Column(name = "result", nullable = false)
  private JobResult jobResult;


  private SyncJobHistory(JobType jobType, IndexInfo indexInfo, Instant targetDate, String worker,
      Instant jobTime, JobResult jobResult) {
    this.jobType = jobType;
    this.indexInfo = indexInfo;
    this.targetDate = targetDate;
    this.worker = worker;
    this.jobTime = jobTime;
    this.jobResult = jobResult;
  }

  public static SyncJobHistory create(JobType jobType, IndexInfo indexInfo, Instant targetDate,
      String worker, Instant jobTime, JobResult jobResult) {
    return new SyncJobHistory(jobType, indexInfo, targetDate, worker, jobTime, jobResult);
  }


}
