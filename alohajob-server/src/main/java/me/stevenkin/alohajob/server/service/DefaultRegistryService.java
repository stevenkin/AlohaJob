package me.stevenkin.alohajob.server.service;

import lombok.extern.slf4j.Slf4j;
import me.stevenkin.alohajob.common.extension.SpiImp;

/**
 * 注册服务的默认实现(什么都不做)
 */
@SpiImp(name = "default")
@Slf4j
public class DefaultRegistryService implements RegistryService{
    @Override
    public void registerServer(String serverAddress) {
        log.info("server {} registered", serverAddress);
    }

    @Override
    public void unregisterServer(String serverAddress) {
        log.info("server {} unregistered", serverAddress);
    }
}
