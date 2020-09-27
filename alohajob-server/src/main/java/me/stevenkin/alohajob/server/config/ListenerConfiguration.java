package me.stevenkin.alohajob.server.config;

import lombok.extern.slf4j.Slf4j;
import me.stevenkin.alohajob.server.ServerAddress;
import me.stevenkin.alohajob.server.listener.ServerBootListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ListenerConfiguration {
    @Autowired
    private AlohaJobServerProperties alohaJobServerProperties;
    @Autowired
    private ServerAddress serverAddress;

    @Bean
    public ServerBootListener serverBootListener() {
        return new ServerBootListener(alohaJobServerProperties, serverAddress);
    }
}
