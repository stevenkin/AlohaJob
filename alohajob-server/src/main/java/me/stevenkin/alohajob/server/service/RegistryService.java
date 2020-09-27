package me.stevenkin.alohajob.server.service;

import me.stevenkin.alohajob.common.extension.Spi;

@Spi("default")
public interface RegistryService {
    /**
     * 向注册中心注册调度服务器
     * @param serverAddress
     */
    void registerServer(String serverAddress);

    /**
     * 向注册中心取消注册调度服务器
     * @param serverAddress
     */
    void unregisterServer(String serverAddress);
}
