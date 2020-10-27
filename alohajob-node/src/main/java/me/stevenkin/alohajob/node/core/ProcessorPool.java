package me.stevenkin.alohajob.node.core;

import me.stevenkin.alohajob.common.Lifecycle;
import me.stevenkin.alohajob.sdk.Processor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class ProcessorPool extends Lifecycle implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    private ConcurrentMap<String, Processor> processors = new ConcurrentHashMap<>();

    public Processor getProcessor(String processorInfo) {
        return processors.get(processorInfo);
    }

    @Override
    protected void doStart() {
        Map<String, Processor> processorMap = applicationContext.getBeansOfType(Processor.class);
        for (Processor processor : processorMap.values()) {
            processors.put(processor.getClass().getCanonicalName(), processor);
        }
    }

    @Override
    protected void doStop() {
        processors.clear();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
