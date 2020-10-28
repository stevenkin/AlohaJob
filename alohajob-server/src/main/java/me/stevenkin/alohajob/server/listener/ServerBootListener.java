package me.stevenkin.alohajob.server.listener;

import lombok.extern.slf4j.Slf4j;
import me.stevenkin.alohajob.common.extension.ExtensionLoader;
import me.stevenkin.alohajob.registry.api.RegistryService;
import me.stevenkin.alohajob.server.ServerAddress;
import me.stevenkin.alohajob.server.config.AlohaJobServerProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServerBootListener implements ApplicationListener<ApplicationEvent>, InitializingBean {
    private RegistryService registryService;
    @Autowired
    private ServerAddress serverAddress;
    @Autowired
    private AlohaJobServerProperties properties;

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ContextRefreshedEvent) {
            start();
        } else if (applicationEvent instanceof ContextClosedEvent) {
            stop();
        }
    }

    private void start() {
        log.info("server {} starting", serverAddress.get());
        registryService.registerServer(serverAddress.get());
    }

    private void stop() {
        log.info("server {} stopping", serverAddress.get());
        registryService.unregisterServer(serverAddress.get());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        registryService = ExtensionLoader.getExtensionLoader(RegistryService.class).getExtension(properties.getRegistryService());
    }
}
