package me.stevenkin.alohajob.server.config;

import me.stevenkin.alohajob.server.cluster.ServerCluster;
import me.stevenkin.alohajob.server.cluster.WorkersClusterManager;
import me.stevenkin.alohajob.server.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClusterConfiguration {
    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public ServerCluster serverCluster() {
        return new ServerCluster(restTemplate);
    }

    @Bean
    public WorkersClusterManager workersClusterManager(AppRepository appRepository) {
        return new WorkersClusterManager(appRepository);
    }
}
