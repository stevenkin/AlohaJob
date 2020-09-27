package me.stevenkin.alohajob.server.cluster;

import me.stevenkin.alohajob.common.model.SystemMetrics;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AppWorkersCluster {
    private Long appId;

    private String appName;

    private ConcurrentMap<String, WorkerStatus> workers = new ConcurrentHashMap<>();

    public AppWorkersCluster(Long appId, String appName) {
        this.appId = appId;
        this.appName = appName;
    }

    public void heartbeat(String workerAddress, SystemMetrics systemMetrics) {
        WorkerStatus workerStatus = workers.computeIfAbsent(workerAddress, WorkerStatus::new);
        workerStatus.setLastHeartbeatTime(new Date().getTime());
        workerStatus.setSystemMetrics(systemMetrics);
    }

    public void outline(String workerAddress) {
        workers.remove(workerAddress);
    }
}
