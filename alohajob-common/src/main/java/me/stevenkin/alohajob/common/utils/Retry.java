package me.stevenkin.alohajob.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.function.Predicate;

@Slf4j
public class Retry {
    /**
     * 重复执行action直到condition为假
     * @param action
     * @param interval
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T execute(Callable<T> action, Condition condition, long interval) throws Exception {
        if (interval < 0)
            throw new IllegalArgumentException();

        Exception e = null;
        while (condition.test()) {
            try {
                return action.call();
            } catch (Exception e1){
                e = e1;
                Thread.sleep(interval);
            }
        }
        throw e;
    }
}
