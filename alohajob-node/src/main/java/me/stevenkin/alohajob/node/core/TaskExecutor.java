package me.stevenkin.alohajob.node.core;

import me.stevenkin.alohajob.common.Lifecycle;

public interface TaskExecutor extends Lifecycle {
    void execute(Long appId, Long jobId, String triggerId);
}
