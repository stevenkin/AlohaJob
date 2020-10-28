package me.stevenkin.alohajob.node;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.stevenkin.alohajob.common.Lifecycle;
import me.stevenkin.alohajob.common.response.NodeStatus;
import me.stevenkin.alohajob.common.response.Response;
import me.stevenkin.alohajob.node.config.AlohaJobNodeProperties;
import me.stevenkin.alohajob.node.core.DefaultTaskExecutor;
import me.stevenkin.alohajob.node.core.ProcessorPool;
import me.stevenkin.alohajob.node.core.SchedulerServerClient;
import me.stevenkin.alohajob.node.service.OnlineService;
import me.stevenkin.alohajob.node.utils.SystemInfoUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
public class AlohaJobNode extends Lifecycle implements InitializingBean, DisposableBean, ApplicationContextAware {
    @Autowired
    private AlohaJobNodeProperties properties;
    @Autowired
    private SchedulerServerClient client;
    @Autowired
    private NodeAddress address;
    @Autowired
    private OnlineService onlineService;
    @Autowired
    @Getter
    private ProcessorPool processorPool;
    @Getter
    private DefaultTaskExecutor taskExecutor;

    private ApplicationContext applicationContext;

    public synchronized void trigger(Long appId, Long jobId, String triggerId) {
        if (!isStarted())
            return;
        taskExecutor.execute(appId, jobId, triggerId);
    }

    public synchronized NodeStatus getStatus() {
        if (!isStarted())
            return null;
        return new NodeStatus(SystemInfoUtils.getSystemMetrics());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }

    @Override
    protected void doStart() {
        processorPool.start();
        taskExecutor = new DefaultTaskExecutor(this);
        taskExecutor.start();
        try {
            onlineService.online();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doStop() {
        try {
            onlineService.outline();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        taskExecutor.stop();
        processorPool.stop();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void destroy() throws Exception {
        stop();
    }
}
