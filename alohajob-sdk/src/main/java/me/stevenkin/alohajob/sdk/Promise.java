package me.stevenkin.alohajob.sdk;

import java.util.concurrent.Future;

public interface Promise<V> extends Future<V> {
    Promise<V> addListener(JobListener listener);

    Promise<V> removeListener(JobListener listener);

    void complete(V v);

    void callbackComplete();
}
