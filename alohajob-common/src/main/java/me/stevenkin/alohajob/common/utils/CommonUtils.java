package me.stevenkin.alohajob.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;
import java.util.concurrent.Callable;

@Slf4j
public class CommonUtils {
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    public static <T> T ignoreException(Callable<T> action) {
        try {
            return action.call();
        } catch (Exception e) {
            return null;
        }
    }

    public static int formatSize(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    public static String genUUID() {
        return StringUtils.replace(UUID.randomUUID().toString(), "-", "");
    }

}
