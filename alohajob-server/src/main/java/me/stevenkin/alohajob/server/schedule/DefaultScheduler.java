package me.stevenkin.alohajob.server.schedule;

import me.stevenkin.alohajob.common.extension.SpiImp;
import me.stevenkin.alohajob.server.config.AlohaJobServerProperties;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.*;

@SpiImp(name = "default")
public class DefaultScheduler implements Scheduler{
    @Autowired
    private AlohaJobServerProperties alohajobServerProperties;

    private ScheduledExecutorService timingService;

    @Override
    public void start() {
        timingService = new ScheduledThreadPoolExecutor(alohajobServerProperties.getScheduleThreadNum());
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
