package me.stevenkin.alohajob.node;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NodeAddress {
    private String host;

    private Integer port;

    public String get() {
        return host + ":" + port;
    }
}
