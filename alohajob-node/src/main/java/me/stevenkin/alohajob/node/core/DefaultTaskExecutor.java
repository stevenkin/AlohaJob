package me.stevenkin.alohajob.node.core;

import lombok.extern.slf4j.Slf4j;
import me.stevenkin.alohajob.common.Lifecycle;
import me.stevenkin.alohajob.common.dto.JobDto;
import me.stevenkin.alohajob.common.dto.JobInstanceDto;
import me.stevenkin.alohajob.common.dto.JobTriggerDto;
import me.stevenkin.alohajob.common.utils.Executors;
import me.stevenkin.alohajob.node.AlohaJobNode;
import me.stevenkin.alohajob.sdk.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;

import static me.stevenkin.alohajob.common.enums.JobTriggerStatus.*;

@Slf4j
public class DefaultTaskExecutor extends Lifecycle implements TaskExecutor {
    private AlohaJobNode node;

    private ExecutorService executor;

    private ConcurrentMap<String, ConcurrentMap<String, Promise<ProcessResult>>> futureMap;

    public DefaultTaskExecutor(AlohaJobNode node) {
        this.node = node;
        this.futureMap = new ConcurrentHashMap<>();
    }

    @Override
    public void execute(Long appId, Long jobId, String triggerId) {
        String address = node.getAddress().toString();
        JobDto jobDto = node.getClient().getJob(jobId);
        executor.submit(() -> {
            for (;;) {
                JobTriggerDto trigger = node.getClient().getJobTrigger(triggerId);
                if (trigger == null) {
                    log.error("trigger must not be null");
                    throw new NullPointerException();
                }
                if (isFinish(of(trigger.getStatus()))) {
                    log.info("trigger {} root instance was finish", triggerId);
                    break;
                }
                JobInstanceDto dto = node.getClient().pullJobInstance(triggerId, address);
                if (dto == null) {
                    log.info("pull trigger {}'s instance is null, maybe no instance assign address {}", triggerId, address);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignore) {

                    }
                    continue;
                }
                if (dto.getIgnore())
                    continue;
                ProcessContext context = buildProcessContext(dto);
                Processor processor = findProcessor(jobDto);
                ProcessResult result;
                try {
                    result = processor.process(context);
                } catch (Exception e) {
                    result = new ProcessResult(ProcessResultType.FAIL, e.getMessage());
                }
                reportResult(dto, result);
                reportIfCallbackComplete(context);
            }
        });
    }

    private void reportIfCallbackComplete(ProcessContext context) {

    }

    private void reportResult(JobInstanceDto dto, ProcessResult result) {

    }

    private Processor findProcessor(JobDto jobDto) {
        return null;
    }

    private ProcessContext buildProcessContext(JobInstanceDto dto) {
        return null;
    }

    public Promise<ProcessResult> getFuture(String parentInstanceId, String subInstanceId) {
        ConcurrentMap<String, Promise<ProcessResult>> futureConcurrentMap = futureMap.get(parentInstanceId);
        if (futureConcurrentMap == null)
            return null;
        return futureConcurrentMap.get(subInstanceId);
    }

    @Override
    public void doStart() {
        executor = Executors.newExecutor(node.getProperties().getExecutorThreadNum(), node.getProperties().getExecutorQueueSize(), "DefaultTaskExecutor-");
    }

    @Override
    public void doStop() {
        executor.shutdown();
    }
}
