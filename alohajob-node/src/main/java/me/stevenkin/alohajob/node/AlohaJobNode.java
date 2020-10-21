package me.stevenkin.alohajob.node;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.stevenkin.alohajob.common.Lifecycle;
import me.stevenkin.alohajob.node.config.AlohaJobNodeProperties;
import me.stevenkin.alohajob.node.core.SchedulerServerClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
public class AlohaJobNode extends Lifecycle implements InitializingBean {
    @Autowired
    private AlohaJobNodeProperties properties;
    @Autowired
    private SchedulerServerClient client;

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }

    @Override
    protected void doStart() {

    }

    @Override
    protected void doStop() {

    }
}
