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

    private String triggerId;

    private String instanceId;

    private String parentInstanceId;

    private String instanceName;

    private String jobParam;

    private String instanceParam;

    private Integer maxRetryTimes;

    private Integer currentRetryTimes;

    private AlohaJobLogger logger;

    public abstract Promise<ProcessResult> newInstance(String instanceName, String instanceParam) throws Exception;

    public abstract boolean isCallbackComplete();

    public abstract Future<?> submit(Runnable task);

    public abstract void sync();

    public abstract void close();
}
