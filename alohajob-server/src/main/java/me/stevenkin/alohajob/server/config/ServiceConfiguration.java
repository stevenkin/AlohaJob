package me.stevenkin.alohajob.server.config;

import me.stevenkin.alohajob.server.ServerAddress;
import me.stevenkin.alohajob.server.cluster.ServerCluster;
import me.stevenkin.alohajob.server.cluster.WorkersClusterManager;
import me.stevenkin.alohajob.server.repository.AppRepository;
import me.stevenkin.alohajob.server.service.AppService;
import me.stevenkin.alohajob.server.service.ServerElectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {
    @Autowired
    private ServerAddress serverAddress;
    @Autowired
    private AlohaJobServerProperties alohaJobServerProperties;

    @Bean
    public AppService appService(AppRepository appRepository, WorkersClusterManager workersClusterManager) {
        return new AppService(appRepository, workersClusterManager);
    }

    @Bean
    public ServerElectionService serverElectionService(AppRepository appRepository, ServerCluster serverCluster) {
        return new ServerElectionService(appRepository, serverAddress, alohaJobServerProperties, serverCluster);
    }
}
