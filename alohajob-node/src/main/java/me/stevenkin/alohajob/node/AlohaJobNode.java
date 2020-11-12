package me.stevenkin.alohajob.node;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.stevenkin.alohajob.common.Lifecycle;
import me.stevenkin.alohajob.common.extension.ExtensionLoader;
import me.stevenkin.alohajob.common.response.NodeStatus;
import me.stevenkin.alohajob.common.supports.ServerRegistryTable;
import me.stevenkin.alohajob.node.config.AlohaJobNodeProperties;
import me.stevenkin.alohajob.node.core.DefaultTaskExecutor;
import me.stevenkin.alohajob.node.core.ProcessorPool;
import me.stevenkin.alohajob.node.core.SchedulerServerClient;
import me.stevenkin.alohajob.node.service.OnlineService;
import me.stevenkin.alohajob.node.utils.SystemInfoUtils;
import me.stevenkin.alohajob.registry.api.DiscoveryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import static me.stevenkin.alohajob.common.Constant.DEFAULT_DISCOVERY_SERVICE;

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
    private ServerRegistryTable registryTable;
    @Autowired
    private OnlineService onlineService;
    @Autowired
    @Getter
    private ProcessorPool processorPool;
    @Getter
    private DefaultTaskExecutor taskExecutor;

    private DiscoveryService discoveryService;

    private ApplicationContext applicationContext;

    public synchronized void trigger(Long appId, Long jobId, String triggerId) {
        if (!isStarted())
            return;
        try {
            taskExecutor.execute(appId, jobId, triggerId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        //0 内部组件初始化
        processorPool.start();
        taskExecutor = new DefaultTaskExecutor(this);
        taskExecutor.start();
        //1 服务发现
        initServerRegistryTable();
        if (discoveryService == null) {
            String discoveryServiceKey = StringUtils.isEmpty(properties.getDiscoveryService()) ? DEFAULT_DISCOVERY_SERVICE : properties.getDiscoveryService();
            discoveryService = ExtensionLoader.getExtensionLoader(DiscoveryService.class).getExtension(discoveryServiceKey);
        }
        discoveryService.subscribe("serverAddressListener", serverAddress -> onlineService.serServers(serverAddress));
        //2 执行器上线
        try {
            onlineService.online();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initServerRegistryTable() {
        properties.getServerAddress().forEach(registryTable::add);
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
