package me.stevenkin.alohajob.server.lock;

import me.stevenkin.alohajob.common.extension.Spi;

import java.util.concurrent.locks.Lock;

@Spi("mysqlLock")
public interface LockService {
    /**
     * 创建锁
     * @param name
     * @return
     */
    Lock getLock(String name);

}
