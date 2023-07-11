package cn.cloudcharts.metadata.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author wuque
 * @title: ReentrantSpinLock
 * @projectName xingyun
 * @description: 可重入自旋锁
 * @date 2023/7/417:06
 */
@Slf4j
public class ReentrantSpinLock {

    private AtomicReference<Thread> sign = new AtomicReference<Thread>();

    public void lock() {
        if (isLock()) {
            return;
        }
        //If the lock is not acquired, it can be spun through CAS
        while (!sign.compareAndSet(null, Thread.currentThread())) {
            // DO nothing
        }
    }

    public void unlock() {
        Thread cur = Thread.currentThread();
        if (isLock()) {
            sign.compareAndSet(cur, null);
        }
        log.info("The lock has been released:{}",cur.getId());
    }

    public boolean isLock() {
        Thread cur = Thread.currentThread();
        return cur == sign.get();
    }
}
