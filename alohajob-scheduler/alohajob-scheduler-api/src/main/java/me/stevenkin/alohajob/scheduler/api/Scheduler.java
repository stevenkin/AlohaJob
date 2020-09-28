package me.stevenkin.alohajob.scheduler.api;

import me.stevenkin.alohajob.common.extension.Spi;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Spi("default")
public interface Scheduler {
    /**
     * 启动调度器
     */
    void start(Map<String, String> config);
    /**
     * 调度定时任务
     */
    ScheduleFuture schedule(Runnable task, long delay, TimeUnit unit);

    /**
     * 停止调度器
     */
    void stop();
}
