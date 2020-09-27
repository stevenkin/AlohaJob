package me.stevenkin.alohajob.server.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnBean(AlohaJobServerConfiguration.Marker.class)
@EnableConfigurationProperties(AlohaJobServerProperties.class)
@Import({ControllerConfiguration.class, ServiceConfiguration.class,ListenerConfiguration.class,
        ClusterConfiguration.class, RepositoryConfiguration.class, RestConfiguration.class, ThreadPoolConfiguration.class})
public class AlohaJobServerAutoConfiguration {

}
