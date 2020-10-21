package me.stevenkin.alohajob.node.core;

import lombok.extern.slf4j.Slf4j;
import me.stevenkin.alohajob.common.Lifecycle;
import me.stevenkin.alohajob.common.utils.Executors;
import me.stevenkin.alohajob.node.AlohaJobNode;

import java.util.concurrent.ExecutorService;

@Slf4j
public class DefaultTaskExecutor extends Lifecycle implements TaskExecutor {
    private AlohaJobNode node;

    private ExecutorService executor;

    public DefaultTaskExecutor(AlohaJobNode node) {
        this.node = node;
    }

    @Override
    public void execute(Long appId, Long jobId, String triggerId) {
        executor.submit(() -> {

        });
    }

    @Override
    public void doStart() {
        executor = Executors.newExecutor(node.getProperties().getExecutorThreadNum(), node.getProperties().getExecutorQueueSize(), "DefaultTaskExecutor-");
    }

    @Override
    public void doStop() {
        executor.shutdown();
    }
}
