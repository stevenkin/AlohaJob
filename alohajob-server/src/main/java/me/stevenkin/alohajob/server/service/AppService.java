package me.stevenkin.alohajob.server.service;

import me.stevenkin.alohajob.server.cluster.WorkersClusterManager;
import me.stevenkin.alohajob.server.model.AppDo;
import me.stevenkin.alohajob.server.repository.AppRepository;
import org.apache.commons.lang3.StringUtils;

public class AppService {
    private AppRepository appRepository;

    private WorkersClusterManager workersClusterManager;

    public AppService(AppRepository appRepository, WorkersClusterManager workersClusterManager) {
        this.appRepository = appRepository;
        this.workersClusterManager = workersClusterManager;
    }

    public Long login(String username, String appName, String secret) {
        AppDo appDo = appRepository.findAppByUsernameAndAppName(username, appName);
        if (appDo == null)
            throw new RuntimeException("no found app");
        if (StringUtils.isEmpty(appDo.getSecret()) && StringUtils.isEmpty(secret) ||
            StringUtils.isNotEmpty(appDo.getSecret()) && StringUtils.isNotEmpty(secret) && secret.equals(appDo.getSecret()))
            return appDo.getId();
        throw new RuntimeException("secret is not current");
    }

    public void logout(Long appId, String workerAddress) {
        AppDo appDo = appRepository.findAppById(appId);
        if (appDo == null)
            throw new RuntimeException("no found app");
        workersClusterManager.outline(appId, workerAddress);
    }
}
