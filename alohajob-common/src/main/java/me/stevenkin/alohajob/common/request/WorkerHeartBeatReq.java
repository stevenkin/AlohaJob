package me.stevenkin.alohajob.common.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.stevenkin.alohajob.common.model.SystemMetrics;

@Data
@AllArgsConstructor
public class WorkerHeartBeatReq {
    private String workerAddress;

    private String username;

    private String appName;

    private Long appId;
    // 时间戳
    private long timestamp;
    //机器负载
    private SystemMetrics systemMetrics;
    // worker 版本
    private String version;
}
