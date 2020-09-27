package me.stevenkin.alohajob.server.service;

import lombok.extern.slf4j.Slf4j;
import me.stevenkin.alohajob.common.extension.ExtensionLoader;
import me.stevenkin.alohajob.server.config.AlohaJobServerProperties;
import me.stevenkin.alohajob.server.schedule.Scheduler;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
public class AlohaJobScheduleService implements InitializingBean, DisposableBean {
    private static final long SCHEDULE_RATE = 15000;

    private Scheduler scheduler;

    private AlohaJobServerProperties alohajobServerProperties;

    @Async("scheduleThreadPool")
    @Scheduled(fixedRate = SCHEDULE_RATE)
    public void scheduleService() {

    }

    @Override
    public void destroy() throws Exception {
        scheduler.stop();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        scheduler = ExtensionLoader.getExtensionLoader(Scheduler.class).getExtension(alohajobServerProperties.getScheduler());
        scheduler.start();
    }
}
