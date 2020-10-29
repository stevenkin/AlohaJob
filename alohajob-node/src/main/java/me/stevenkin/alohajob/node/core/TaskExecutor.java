package me.stevenkin.alohajob.node.core;

public interface TaskExecutor {
    void execute(Long appId, Long jobId, String triggerId) throws Exception;
}
