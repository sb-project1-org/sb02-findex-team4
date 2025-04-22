package com.sprint.findex.sb02findexteam4.sync.entity;

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
    @Column(name = "job_type", columnDefinition = "job_type", nullable = false)
    private JobType jobType;

//  @ManyToOne
// @JoinColumn(name = "index_info_id")
//    private IndexInfo indexInfo;

    @Timestamp
    @Column(name = "target_date", nullable = false)
    private Instant targetDate;

    @Column(name = "worker",nullable = false)
    private String worker;

    @Timestamp
    @Column(name = "job_time",nullable = false)
    private Instant jobTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "result", columnDefinition = "job_result", nullable = false)
    private JobResult jobResult;

    /*
    private SyncJobHistory(JobType jobType, IndexInfo indexinfo, Instant targetDate, String worker, Instant jobTime, JobResult jobResult) {
        this.jobType = jobType;
        this.indexinfo = indexinfo;
        this.targetDate = targetDate;
        this.worker = worker;
        this.jobTime = jobTime;
        this.jobResult = jobResult;
    }

    public static SyncJobHistory create(JobType jobType, IndexInfo indexinfo, Instant targetDate, String worker, Instant jobTime, JobResult jobResult) {
        return new SyncJobHistory(jobType, indexinfo,targetDate, worker, jobTime, jobResult);
    }

     */
}
