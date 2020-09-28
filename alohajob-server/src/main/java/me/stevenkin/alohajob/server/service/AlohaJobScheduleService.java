package me.stevenkin.alohajob.server.service;

import lombok.extern.slf4j.Slf4j;
import me.stevenkin.alohajob.common.Constant;
import me.stevenkin.alohajob.common.extension.ExtensionLoader;
import me.stevenkin.alohajob.scheduler.api.Scheduler;
import me.stevenkin.alohajob.server.config.AlohaJobServerProperties;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
public class AlohaJobScheduleService implements InitializingBean, DisposableBean {
    private static final long SCHEDULE_RATE = 15000;
    @Autowired
    private AlohaJobServerProperties alohajobServerProperties;

    private Scheduler scheduler;

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
        scheduler.start(new HashMap<String, String>() {{
            put(Constant.SCHEDULER_THREAD_NUM, alohajobServerProperties.getScheduleThreadNum().toString());
        }});
    }
}
