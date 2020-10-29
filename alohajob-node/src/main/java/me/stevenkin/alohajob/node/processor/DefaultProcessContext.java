package me.stevenkin.alohajob.node.processor;

import lombok.Setter;
import me.stevenkin.alohajob.common.dto.JobInstanceDto;
import me.stevenkin.alohajob.common.dto.JobInstanceResultDto;
import me.stevenkin.alohajob.common.utils.Executors;
import me.stevenkin.alohajob.node.core.ProcessResultPromise;
import me.stevenkin.alohajob.node.core.SchedulerServerClient;
import me.stevenkin.alohajob.node.utils.DtoUtils;
import me.stevenkin.alohajob.sdk.Promise;
import me.stevenkin.alohajob.sdk.ProcessContext;
import me.stevenkin.alohajob.sdk.ProcessResult;
import org.springframework.beans.BeanUtils;

import java.util.concurrent.*;

import static me.stevenkin.alohajob.common.enums.JobInstanceStatus.*;

public class DefaultProcessContext extends ProcessContext {
    private JobInstanceDto dto;

    private SchedulerServerClient client;

    private ConcurrentMap<String, Promise<ProcessResult>> futureMap;

    private ScheduledExecutorService service;

    private ExecutorService executorService;

    public DefaultProcessContext(JobInstanceDto dto, SchedulerServerClient client, ConcurrentMap<String, Promise<ProcessResult>> map) {
        this.dto = dto;
        this.client = client;
        this.futureMap = map;

        BeanUtils.copyProperties(dto, this);

        service = new ScheduledThreadPoolExecutor(10);

        executorService = Executors.newExecutor(10, 1000, "taskExecutorService-");

        setLogger();
    }

    private void setLogger() {

    }

    @Override
    public Promise<ProcessResult> newInstance(String instanceName, String instanceParam) throws Exception {
        String instanceId = client.newJobInstance(getTriggerId(), getInstanceId(), instanceName, instanceParam);
        JobInstanceStatusChecker checker = new JobInstanceStatusChecker(instanceId);
        Promise<ProcessResult> future = buildFuture(instanceId, dto, checker);
        futureMap.put(instanceId, future);
        ScheduledFuture<?> scheduledFuture = service.schedule(checker, 60, TimeUnit.SECONDS);
        checker.setFuture(scheduledFuture);
        checker.start();
        return future;
    }

    private Promise<ProcessResult> buildFuture(String instanceId, JobInstanceDto dto, JobInstanceStatusChecker checker) {
        return new ProcessResultPromise(instanceId, dto, checker, client, futureMap);
    }

    @Override
    public boolean isCallbackComplete() {
        return futureMap.isEmpty();
    }

    @Override
    public Future<?> submit(Runnable task) {
        return executorService.submit(task);
    }

    @Override
    public void sync() {
        for (;;) {
            try {
                if (executorService.awaitTermination(2, TimeUnit.SECONDS))
                    break;
            } catch (InterruptedException ignore) {

            }
        }
    }

    @Override
    public void close() {
        service.shutdown();
        executorService.shutdown();
    }

    public class JobInstanceStatusChecker implements Runnable {
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
                JobInstanceDto instanceDto = client.getJobInstance(instanceId);
                if (instanceDto == null || !(isFinish(of(instanceDto.getStatus())) || instanceDto.getCallbackFinish()))
                    return;
                Promise<ProcessResult> future1;
                boolean isFinish = true;
                if (instanceDto.getCallbackFinish()) {
                    isFinish = false;
                }
                future1 = futureMap.get(instanceId);
                if (future1 == null)
                    return;
                JobInstanceResultDto resultDto = client.getJobInstanceResult(instanceId);
                if (resultDto == null)
                    throw new NullPointerException();
                ProcessResult result = DtoUtils.toProcessResult(resultDto);
                if (isFinish)
                    if (isCancel(of(instanceDto.getStatus())))
                        future1.cancel();
                    else
                        future1.complete(result);
                else
                    future1.callbackComplete();
            } catch (Exception e) {
                //TODO 告警
            }
        }

        public void start() {
            latch.countDown();
        }

        public void stop() {
            future.cancel(true);
        }
    }
}
