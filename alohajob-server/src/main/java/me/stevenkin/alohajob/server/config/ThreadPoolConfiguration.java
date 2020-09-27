package me.stevenkin.alohajob.server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Slf4j
@EnableAsync
@Configuration
public class ThreadPoolConfiguration {
    @Bean("scheduleThreadPool")
    public Executor getTimingPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 2);
        // use SynchronousQueue
        executor.setQueueCapacity(0);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("scheduleThreadPool-");
        executor.setRejectedExecutionHandler((r, e) -> {
            log.warn("[ScheduleService] timing pool can't schedule job immediately, maybe some job using too much cpu times.");
            new Thread(r).start();
        });
        return executor;
    }
}
