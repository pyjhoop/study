package com.study.synchronize;

public class SynchronizedIncreaser {
    private int counter = 0;

    public synchronized void increase() {
        counter++;
    }

    public synchronized  int getCounter() {
        return counter;
    }
}
