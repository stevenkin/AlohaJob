package me.stevenkin.alohajob.sdk;

import java.util.concurrent.Future;

public interface AlohaFuture<V> extends Future<V> {
    void addListener(JobListener listener);

    void complete(V v);

    void callbackComplete();
}
