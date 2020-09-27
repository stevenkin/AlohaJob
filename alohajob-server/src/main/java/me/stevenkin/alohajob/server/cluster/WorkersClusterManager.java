package me.stevenkin.alohajob.server.cluster;

import me.stevenkin.alohajob.common.model.SystemMetrics;
import me.stevenkin.alohajob.server.model.AppDo;
import me.stevenkin.alohajob.server.repository.AppRepository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WorkersClusterManager {
    private ConcurrentMap<Long, AppWorkersCluster> appWorkersClusterMap = new ConcurrentHashMap<>();

    private AppRepository appRepository;

    public WorkersClusterManager(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public void outline(Long appId, String workerAddress) {
        if (!appWorkersClusterMap.containsKey(appId))
            return;
        appWorkersClusterMap.get(appId).outline(workerAddress);
    }

    public void heartbeat(Long appId, String workerAddress, SystemMetrics systemMetrics) {
        AppWorkersCluster appWorkersCluster = appWorkersClusterMap.computeIfAbsent(appId, id -> {
            AppDo appDo = appRepository.findAppById(id);
            if (appDo == null)
                throw new RuntimeException("no found app");
            return new AppWorkersCluster(id, appDo.getAppName());
        });
        appWorkersCluster.heartbeat(workerAddress, systemMetrics);
    }
}
