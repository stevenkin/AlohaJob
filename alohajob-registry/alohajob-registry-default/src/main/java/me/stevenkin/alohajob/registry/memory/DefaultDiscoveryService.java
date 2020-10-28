package me.stevenkin.alohajob.registry.memory;

import me.stevenkin.alohajob.common.extension.SpiImp;
import me.stevenkin.alohajob.common.supports.ServerRegistryTable;
import me.stevenkin.alohajob.registry.api.DiscoveryService;
import me.stevenkin.alohajob.registry.api.NotifyListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SpiImp(name = "default")
public class DefaultDiscoveryService implements DiscoveryService {
    private ConcurrentMap<String, NotifyListener> listenerMap = new ConcurrentHashMap<>();
    @Autowired
    private ServerRegistryTable serverRegistryTable;

    @Override
    public void subscribe(String name, NotifyListener listener) {
        listenerMap.put(name, listener);
        listener.notify(new ArrayList<>(serverRegistryTable.getServerTable()));
    }

    @Override
    public void unsubscribe(String name) {
        listenerMap.remove(name);
    }
}
