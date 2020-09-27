package me.stevenkin.alohajob.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

@Slf4j
public class Retry {
    /**
     * 重复执行action tryNum次，如果tryNum=-1,则执行无限次
     * @param action
     * @param tryNum
     * @param interval
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T execute(Callable<T> action, int tryNum, long interval) throws Exception {
        if (interval < 0)
            throw new IllegalArgumentException();

        Exception e = null;
        for (int i = 0; tryNum < 0 || i < tryNum; i++) {
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
