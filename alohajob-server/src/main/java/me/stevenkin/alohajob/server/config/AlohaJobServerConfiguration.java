package me.stevenkin.alohajob.server.config;

import lombok.extern.slf4j.Slf4j;
import me.stevenkin.alohajob.common.extension.ExtensionLoader;
import me.stevenkin.alohajob.common.utils.NetUtils;
import me.stevenkin.alohajob.server.ServerAddress;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AlohaJobServerProperties.class)
@Slf4j
public class AlohaJobServerConfiguration implements ApplicationContextAware {
    @Value("${server.port}")
    private Integer port;

    @Bean
    public ServerAddress serverAddress() {
        log.info("server address");
        return new ServerAddress(NetUtils.getLocalHost(), port);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ExtensionLoader.setApplicationContext(applicationContext);
    }
}
