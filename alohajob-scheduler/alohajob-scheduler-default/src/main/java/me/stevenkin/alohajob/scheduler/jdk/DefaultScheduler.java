package me.stevenkin.alohajob.scheduler.jdk;

import me.stevenkin.alohajob.common.Constant;
import me.stevenkin.alohajob.common.extension.SpiImp;
import me.stevenkin.alohajob.scheduler.api.ScheduleFuture;
import me.stevenkin.alohajob.scheduler.api.Scheduler;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.*;

@SpiImp(name = "default")
public class DefaultScheduler implements Scheduler {
    private ScheduledExecutorService timingService;

    @Override
    public void start(Map<String, String> config) {
        timingService = new ScheduledThreadPoolExecutor(StringUtils.isNotEmpty(config.get(Constant.SCHEDULER_THREAD_NUM)) ? Integer.parseInt(config.get(Constant.SCHEDULER_THREAD_NUM)) : 10);
    }

    @Override
    public ScheduleFuture schedule(Runnable task, long delay, TimeUnit unit) {
        Future future = timingService.schedule(task, delay, unit);
        return new ScheduleFuture() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return future.cancel(mayInterruptIfRunning);
            }

            @Override
            public boolean isCancelled() {
                return future.isCancelled();
            }

            @Override
            public boolean isDone() {
                return future.isDone();
            }

            @Override
            public Void get() throws InterruptedException, ExecutionException {
                throw new RuntimeException();
            }

            @Override
            public Void get(long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                throw new RuntimeException();
            }
        };
    }

    @Override
    public void stop() {
        timingService.shutdown();
    }
}
