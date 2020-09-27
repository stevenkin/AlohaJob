package me.stevenkin.alohajob.server.cluster;

import lombok.Data;
import me.stevenkin.alohajob.common.model.SystemMetrics;

@Data
public class WorkerStatus {
    private String workerAddress;

    private long lastHeartbeatTime = -1L;

    private SystemMetrics systemMetrics;

    public WorkerStatus(String workerAddress) {
        this.workerAddress = workerAddress;
    }
}
