package me.stevenkin.alohajob.registry.api;

import me.stevenkin.alohajob.common.extension.Spi;

@Spi("default")
public interface DiscoveryService {
    /**
     * 添加与name关联的listener
     * @param name
     * @param listener
     */
    void subscribe(String name, NotifyListener listener);

    /**
     * 移除与name关联的listener
     * @param name
     */
    void unsubscribe(String name);
}
