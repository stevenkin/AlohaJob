package me.stevenkin.alohajob.sdk;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.stevenkin.alohajob.common.logger.AlohaJobLogger;

@Getter
@Setter
@ToString
public class ProcessContext {
    private Long jobId;

    private Long instanceId;

    private Long shardId;

    private String jobParam;

    private String instanceParam;

    private String shardParam;

    private Integer totalShardCount;

    private Integer maxRetryTimes;

    private Integer currentRetryTimes;

    private AlohaJobLogger logger;
}
