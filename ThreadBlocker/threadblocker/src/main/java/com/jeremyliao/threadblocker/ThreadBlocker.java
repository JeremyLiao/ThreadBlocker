package com.jeremyliao.threadblocker;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * Created by liaohailiang on 2019/3/15.
 */
public class ThreadBlocker {

    private static final class Sync extends AbstractQueuedSynchronizer {

        int getCount() {
            return getState();
        }

        protected int tryAcquireShared(int acquires) {
            for (; ; ) {
                int c = getState();
                int nextc = c + 1;
                if (compareAndSetState(c, nextc))
                    return -1;
            }
        }

        protected boolean tryReleaseShared(int releases) {
            for (; ; ) {
                int c = getState();
                if (c == 0)
                    return false;
                int nextc = 0;
                if (compareAndSetState(c, nextc))
                    return true;
            }
        }
    }

    private final Sync sync;


    public ThreadBlocker() {
        this.sync = new Sync();
    }

    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    public boolean await(long timeout, TimeUnit unit)
            throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
    }

    public void release() {
        sync.releaseShared(1);
    }

    public String toString() {
        return super.toString() + "[Count = " + sync.getCount() + "]";
    }
}
