package me.stevenkin.alohajob.registry.api;

import java.util.List;

public interface NotifyListener {
    /**
     * 当调度服务器发生变化时(上下线)进行通知
     * @param serverAddress
     */
    void notify(List<String> serverAddress);
}
