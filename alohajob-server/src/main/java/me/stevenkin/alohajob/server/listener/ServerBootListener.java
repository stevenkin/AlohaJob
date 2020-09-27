package me.stevenkin.alohajob.server.listener;

import lombok.extern.slf4j.Slf4j;
import me.stevenkin.alohajob.common.extension.ExtensionLoader;
import me.stevenkin.alohajob.server.ServerAddress;
import me.stevenkin.alohajob.server.config.AlohaJobServerProperties;
import me.stevenkin.alohajob.server.service.RegistryService;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

@Slf4j
public class ServerBootListener implements ApplicationListener<ApplicationEvent> {
    private RegistryService registryService;

    private ServerAddress serverAddress;

    private AlohaJobServerProperties AlohaJobServerProperties;

    public ServerBootListener(AlohaJobServerProperties AlohaJobServerProperties, ServerAddress serverAddress) {
        this.AlohaJobServerProperties = AlohaJobServerProperties;
        this.serverAddress = serverAddress;
    }

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
        registryService = ExtensionLoader.getExtensionLoader(RegistryService.class).getExtension(AlohaJobServerProperties.getRegistryService());
        registryService.registerServer(serverAddress.get());
        log.info("server {} started", serverAddress.get());
    }

    private void stop() {
        log.info("server {} stopping", serverAddress.get());
        registryService.unregisterServer(serverAddress.get());
        log.info("server {} stoped", serverAddress.get());
    }

}
