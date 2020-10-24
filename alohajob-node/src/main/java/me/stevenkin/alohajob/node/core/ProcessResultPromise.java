package me.stevenkin.alohajob.node.core;

import me.stevenkin.alohajob.common.dto.JobInstanceDto;
import me.stevenkin.alohajob.node.processor.DefaultProcessContext;
import me.stevenkin.alohajob.sdk.JobListener;
import me.stevenkin.alohajob.sdk.ProcessResult;
import me.stevenkin.alohajob.sdk.Promise;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ProcessResultPromise implements Promise<ProcessResult> {
    private String instanceId;

    private JobInstanceDto dto;

    private DefaultProcessContext.JobInstanceStatusChecker checker;

    private SchedulerServerClient client;

    private ConcurrentMap<String, Promise<ProcessResult>> futureMap;

    private List<JobListener> listeners;

    private volatile ProcessResult result;

    private volatile boolean isCompleted;

    public ProcessResultPromise(String instanceId, JobInstanceDto dto, DefaultProcessContext.JobInstanceStatusChecker checker, SchedulerServerClient client, ConcurrentMap<String, Promise<ProcessResult>> futureMap) {
        this.instanceId = instanceId;
        this.dto = dto;
        this.checker = checker;
        this.client = client;
        this.futureMap = futureMap;
        this.listeners = new ArrayList<>();
        this.isCompleted = false;
    }

    @Override
    public synchronized Promise<ProcessResult> addListener(JobListener listener) {
        listeners.add(listener);
        return this;
    }

    @Override
    public synchronized Promise<ProcessResult> removeListener(JobListener listener) {
        Iterator<JobListener> iterator = listeners.iterator();
        while (iterator.hasNext()) {
            JobListener listener1 = iterator.next();
            if (listener1 == listener) {
                 iterator.remove();
                 break;
            }
        }
        return this;
    }

    @Override
    public synchronized void complete(ProcessResult result) {
        this.result = result;
        this.isCompleted = true;
        notifyAll();
    }

    @Override
    public void callbackComplete() {
        futureMap.remove(instanceId);
        if (futureMap.isEmpty()) {
            client.callbackCompleteInstance(dto.getInstanceId());
        }
    }

    @Override
    public synchronized void cancel() {
        if (isCompleted)
            return;
        isCompleted = true;
        result = null;
        notifyAll();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (isCancelled())
            return true;
        if (!mayInterruptIfRunning)
            return false;
        for (;;) {
            if (client.cancelInstance(instanceId))
                break;
        }
        for (;;) {
            synchronized (this) {
                if (isCancelled())
                    return true;
                try {
                    wait();
                } catch (InterruptedException e) {

                }
            }
        }
    }

    @Override
    public boolean isCancelled() {
        return isDone() && result == null;
    }

    @Override
    public boolean isDone() {
        return isCompleted;
    }

    @Override
    public ProcessResult get() throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public ProcessResult get(long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}
