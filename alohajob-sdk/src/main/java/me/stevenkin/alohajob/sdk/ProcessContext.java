package me.stevenkin.alohajob.sdk;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.stevenkin.alohajob.common.logger.AlohaJobLogger;

@Getter
@Setter
@ToString
public abstract class ProcessContext {
    private Long jobId;

    private String triggerId;

    private String instanceId;

    private String parentInstanceId;

    private String instanceName;

    private String jobParam;

    private String instanceParam;

    private Integer maxRetryTimes;

    private Integer currentRetryTimes;

    private AlohaJobLogger logger;

    public abstract AlohaFuture<ProcessResult> newInstance(String instanceName, String instanceParam);

    public abstract AlohaFuture<ProcessResult> newInstanceIfAbsent(String instanceName, String instanceParam);

    public abstract void close();
}
