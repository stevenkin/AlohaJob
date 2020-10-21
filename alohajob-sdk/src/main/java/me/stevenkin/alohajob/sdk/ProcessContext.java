package me.stevenkin.alohajob.sdk;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.stevenkin.alohajob.common.logger.AlohaJobLogger;

import java.util.concurrent.Future;

@Getter
@Setter
@ToString
public abstract class ProcessContext {
    private Long jobId;

    private Long instanceId;

    private String instanceName;

    private String jobParam;

    private String instanceParam;

    private Integer maxRetryTimes;

    private Integer currentRetryTimes;

    private AlohaJobLogger logger;

    public abstract Future<String> newInstance(String instanceName, String instanceParam);

    public abstract Future<String> newInstanceIfAbsent(String instanceName, String instanceParam);
}
