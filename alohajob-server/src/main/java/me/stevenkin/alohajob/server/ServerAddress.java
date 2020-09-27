package me.stevenkin.alohajob.server;

import lombok.Getter;

@Getter
public class ServerAddress {
    private String host;

    private Integer port;

    public ServerAddress(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public String get() {
        return host + ":" + port;
    }
}
