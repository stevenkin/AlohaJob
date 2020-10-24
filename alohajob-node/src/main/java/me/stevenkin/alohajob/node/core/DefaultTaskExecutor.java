package me.stevenkin.alohajob.node.core;

import lombok.extern.slf4j.Slf4j;
import me.stevenkin.alohajob.common.Lifecycle;
import me.stevenkin.alohajob.common.dto.JobDto;
import me.stevenkin.alohajob.common.dto.JobInstanceDto;
import me.stevenkin.alohajob.common.dto.JobInstanceResultDto;
import me.stevenkin.alohajob.common.dto.JobTriggerDto;
import me.stevenkin.alohajob.common.utils.Executors;
import me.stevenkin.alohajob.common.utils.Holder;
import me.stevenkin.alohajob.node.AlohaJobNode;
import me.stevenkin.alohajob.node.utils.DtoUtils;
import me.stevenkin.alohajob.sdk.*;

import java.util.concurrent.*;

import static me.stevenkin.alohajob.common.enums.JobTriggerStatus.*;

@Slf4j
public class DefaultTaskExecutor extends Lifecycle implements TaskExecutor {
    private AlohaJobNode node;

    private ExecutorService executor;

    private ExecutorService instanceExecutor;

    private ConcurrentMap<String, ConcurrentMap<String, Promise<ProcessResult>>> futureMap;

    private ConcurrentMap<String, Future<ProcessResult>> resultFutureMap;

    public DefaultTaskExecutor(AlohaJobNode node) {
        this.node = node;
        this.futureMap = new ConcurrentHashMap<>();
        this.resultFutureMap = new ConcurrentHashMap<>();
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
                CountDownLatch latch = new CountDownLatch(1);
                Future<ProcessResult> resultFuture = instanceExecutor.submit(() -> {
                    try {
                        latch.await();
                    } catch (Exception ignore) {

                    }
                    ProcessResult result1;
                    try {
                        result1 = processor.process(context);
                    } catch (Exception e) {
                        result1 = new ProcessResult(ProcessResultType.FAIL, e.getMessage());
                    }
                    return result1;
                });
                resultFutureMap.put(context.getInstanceId(), resultFuture);
                latch.countDown();
                ProcessResult result = null;
                try {
                    result = resultFuture.get();
                    reportResult(dto, result);
                } catch (Exception ignore) {
                    //抛了异常，说明实例执行进程被中断了,对应的数据库job instance记录已经被设置为CNACEL，因此忽略这个异常
                }

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
        executor = Executors.newExecutor(node.getProperties().getExecutorThreadNum(), node.getProperties().getExecutorQueueSize(), "defaultTaskExecutor-");
        instanceExecutor = Executors.newExecutor(node.getProperties().getExecutorThreadNum() * 5, node.getProperties().getExecutorQueueSize() * 10, "instanceTaskExecutor-");
    }

    @Override
    public void doStop() {
        executor.shutdown();
        instanceExecutor.shutdown();
    }
}
