package com.study.atomic;

import com.study.lock.LockedIncreaser;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AtomicCurrencyTest {

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        int threads = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch countDownLatch = new CountDownLatch(threads);
        AtomicIncreaser increaser = new AtomicIncreaser();

        for(int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    for(int j = 0; j < 100000; j ++){
                        increaser.increase();
                    }
                }finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        long end = System.currentTimeMillis();
        executor.shutdown();
        System.out.println("spend time : " + (end - start));
        System.out.println("result : " + increaser.getCount());
    }
}
