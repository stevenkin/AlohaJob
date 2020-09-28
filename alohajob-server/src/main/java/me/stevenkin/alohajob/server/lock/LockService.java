package me.stevenkin.alohajob.server.lock;

import java.util.concurrent.locks.Lock;

public interface LockService {
    /**
     * 创建锁
     * @param name
     * @return
     */
    Lock getLock(String name);

}
