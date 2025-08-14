package com.study.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockedIncreaser {
    Lock lock = new ReentrantLock();

    private int count = 0;

    public void increase() {
        lock.lock();
        try {
            this.count++;
        }finally {
            lock.unlock();
        }
    }

    public int getCount() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }
}
