package me.stevenkin.alohajob.node;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.stevenkin.alohajob.common.Lifecycle;
import me.stevenkin.alohajob.node.config.AlohaJobNodeProperties;
import me.stevenkin.alohajob.node.core.DefaultTaskExecutor;
import me.stevenkin.alohajob.node.core.ProcessorPool;
import me.stevenkin.alohajob.node.core.SchedulerServerClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
public class AlohaJobNode extends Lifecycle implements InitializingBean, ApplicationContextAware {
    @Autowired
    private AlohaJobNodeProperties properties;
    @Autowired
    private SchedulerServerClient client;
    @Autowired
    private NodeAddress address;
    @Autowired
    private ProcessorPool processorPool;
    @Getter
    private DefaultTaskExecutor taskExecutor;

    private ApplicationContext applicationContext;

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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
