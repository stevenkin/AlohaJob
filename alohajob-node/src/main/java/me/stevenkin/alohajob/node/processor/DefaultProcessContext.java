package me.stevenkin.alohajob.node.processor;

import lombok.Setter;
import me.stevenkin.alohajob.common.dto.JobInstanceDto;
import me.stevenkin.alohajob.common.dto.JobInstanceResultDto;
import me.stevenkin.alohajob.node.core.SchedulerServerClient;
import me.stevenkin.alohajob.node.utils.DtoUtils;
import me.stevenkin.alohajob.sdk.AlohaFuture;
import me.stevenkin.alohajob.sdk.ProcessContext;
import me.stevenkin.alohajob.sdk.ProcessResult;
import me.stevenkin.alohajob.sdk.ProcessResultType;
import org.springframework.beans.BeanUtils;

import java.util.concurrent.*;

import static me.stevenkin.alohajob.common.enums.JobInstanceStatus.*;

public class DefaultProcessContext extends ProcessContext {
    private JobInstanceDto dto;

    private SchedulerServerClient client;

    private ConcurrentMap<String, AlohaFuture<ProcessResult>> futureMap;

    private ScheduledExecutorService service;

    public DefaultProcessContext(JobInstanceDto dto, SchedulerServerClient client, ConcurrentMap<String, AlohaFuture<ProcessResult>> map) {
        this.dto = dto;
        this.client = client;
        this.futureMap = map;

        BeanUtils.copyProperties(dto, this);

        service = new ScheduledThreadPoolExecutor(10);

        setLogger();
    }

    private void setLogger() {

    }

    @Override
    public AlohaFuture<ProcessResult> newInstance(String instanceName, String instanceParam) {
        String instanceId = client.newJobInstance(getTriggerId(), getInstanceId(), instanceName, instanceParam);
        JobInstanceStatusChecker checker = new JobInstanceStatusChecker(instanceId);
        AlohaFuture<ProcessResult> future = buildFuture(dto, checker);
        futureMap.put(instanceId, future);
        ScheduledFuture<?> scheduledFuture = service.schedule(checker, 60, TimeUnit.SECONDS);
        checker.setFuture(scheduledFuture);
        checker.start();
        return future;
    }

    private AlohaFuture<ProcessResult> buildFuture(JobInstanceDto dto, JobInstanceStatusChecker checker) {
        // new AlohaFuture<ProcessResult>(dto, checker, futureMap)
        return null;
    }

    @Override
    public AlohaFuture<ProcessResult> newInstanceIfAbsent(String instanceName, String instanceParam) {
        return null;
    }

    @Override
    public void close() {
        service.shutdown();
    }

    private class JobInstanceStatusChecker implements Runnable {
        private String instanceId;
        @Setter
        private ScheduledFuture<?> future;

        private CountDownLatch latch;

        public JobInstanceStatusChecker(String instanceId) {
            this.instanceId = instanceId;
            this.latch = new CountDownLatch(1);
        }

        @Override
        public void run() {
            try {
                latch.await();
            } catch (InterruptedException ignore) {

            }
            JobInstanceDto instanceDto = client.getJobInstance(instanceId);
            if (instanceDto == null || !(isFinish(of(instanceDto.getStatus())) || instanceDto.getCallbackFinish()))
                return;
            AlohaFuture<ProcessResult> future1;
            boolean isFinish = true;
            if (instanceDto.getCallbackFinish()) {
                future1 = futureMap.remove(instanceId);
                isFinish = false;
            } else {
                future1 = futureMap.get(instanceId);
            }
            if (future1 == null)
                return;
            JobInstanceResultDto resultDto = client.getJobInstanceResult(instanceId);
            if (resultDto == null)
                throw new NullPointerException();
            ProcessResult result = DtoUtils.toProcessResult(resultDto);
            if (isFinish)
                future1.complete(result);
            else
                future1.callbackComplete();
        }

        public void start() {
            latch.countDown();
        }

        public void stop() {
            future.cancel(true);
        }
    }
}
