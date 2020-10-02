package me.stevenkin.alohajob.sdk;

import lombok.*;
import me.stevenkin.alohajob.common.model.InstanceShard;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProcessResult {
    private boolean success;
    private String msg;

    private List<InstanceShard> shards;

    public void addShard(InstanceShard shard) {
        shards.add(shard);
    }

    public void addShards(List<InstanceShard> shardList) {
        shardList.forEach(this::addShard);
    }
}
