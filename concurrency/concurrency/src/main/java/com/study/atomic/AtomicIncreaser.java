package com.study.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIncreaser {
    private AtomicInteger count = new AtomicInteger();

    public void increase() {
        count.getAndIncrement();
    }

    public int getCount() {
        return count.get();
    }
}
