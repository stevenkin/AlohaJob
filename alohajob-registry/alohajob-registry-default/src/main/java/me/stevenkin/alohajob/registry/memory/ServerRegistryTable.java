package me.stevenkin.alohajob.registry.memory;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Component
public class ServerRegistryTable {
    private Set<String> serverTable = new ConcurrentSkipListSet<>();

    public void add(String server) {
        serverTable.add(server);
    }

    public void remove(String server) {
        serverTable.remove(server);
    }

    public Set<String> getServerTable() {
        return Collections.unmodifiableSet(serverTable);
    }
}
