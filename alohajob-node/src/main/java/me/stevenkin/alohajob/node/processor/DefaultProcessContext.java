package me.stevenkin.alohajob.node.processor;

import me.stevenkin.alohajob.sdk.ProcessContext;

import java.util.concurrent.Future;

public class DefaultProcessContext extends ProcessContext {
    @Override
    public Future<String> newInstance(String instanceName, String instanceParam) {
        return null;
    }

    @Override
    public Future<String> newInstanceIfAbsent(String instanceName, String instanceParam) {
        return null;
    }
}
