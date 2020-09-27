package me.stevenkin.alohajob.server.schedule;

import me.stevenkin.alohajob.common.extension.Spi;

import java.util.concurrent.TimeUnit;

@Spi("default")
public interface Scheduler {
    /**
     * 启动调度器
     */
    void start();
    /**
     * 调度定时任务
     */
    ScheduleFuture schedule(Runnable task, long delay, TimeUnit unit);

    /**
     * 停止调度器
     */
    void stop();
}
