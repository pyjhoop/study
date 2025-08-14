package com.study.readWriteLock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 쓰기 락은 하나의 스레드만 구할수 있다.
 * 읽기 락은 여러 스레드가 구할수 있다.
 * 한 스레드가 쓰기 락을 획득했으면 쓰기 락이 해제될때까지 읽기 잠금을 구할수 없다.
 * 읽기 잠금을 획득한 모든 스레드가 읽기 잠금을 해제할 때까지 쓰기 잠금을 할 수 없다.
 */

public class ReadWriteLockIncreaser {
    private int count = 0;
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    Lock readLock = lock.readLock();
    Lock writeLock = lock.writeLock();

    public void increase() {
        writeLock.lock();
        try {
            count++;
        } finally {
            writeLock.unlock();
        }
    }

    public int getCount() {
        readLock.lock();
        try {
            return count;
        } finally {
            readLock.unlock();
        }
    }
}
