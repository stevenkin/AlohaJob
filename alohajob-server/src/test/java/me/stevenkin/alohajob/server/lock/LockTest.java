package me.stevenkin.alohajob.server.lock;

import lombok.extern.slf4j.Slf4j;
import me.stevenkin.alohajob.common.extension.ExtensionLoader;
import me.stevenkin.alohajob.server.EnableAlohaJobServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.locks.Lock;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAlohaJobServer
@Slf4j
public class LockTest {
    @Test
    public void lockTest() {
        LockService lockService = ExtensionLoader.getExtensionLoader(LockService.class).getExtension(null);
        Lock lock = lockService.getLock("3");
        lock.lock();
        try {
            log.info("sync doing...");
        } finally {
            lock.unlock();
        }
    }
}