package me.stevenkin.alohajob.common;

public abstract class Lifecycle {
    private boolean isStarted = false;

    public synchronized void start() {
        if (isStarted)
            return;
        doStart();
        isStarted = true;
    }

    protected abstract void doStart();

    public boolean isStarted() {
        return isStarted;
    }

    public synchronized void stop() {
        if (!isStarted)
            return;
        doStop();
        isStarted = false;
    }

    protected abstract void doStop();

    public boolean isStopped() {
        return !isStarted;
    }
}
