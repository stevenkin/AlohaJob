package me.stevenkin.alohajob.registry.memory;

import lombok.extern.slf4j.Slf4j;
import me.stevenkin.alohajob.common.extension.SpiImp;
import me.stevenkin.alohajob.registry.api.RegistryService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 注册服务的默认实现(什么都不做)
 */
@SpiImp(name = "default")
@Slf4j
public class DefaultRegistryService implements RegistryService {
    @Autowired
    private ServerRegistryTable serverRegistryTable;

    @Override
    public void registerServer(String serverAddress) {
        log.info("server {} registered", serverAddress);
        serverRegistryTable.add(serverAddress);
    }

    @Override
    public void unregisterServer(String serverAddress) {
        log.info("server {} unregistered", serverAddress);
        serverRegistryTable.remove(serverAddress);
    }
}
