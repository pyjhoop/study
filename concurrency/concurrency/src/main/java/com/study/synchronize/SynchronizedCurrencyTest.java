package com.study.synchronize;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SynchronizedCurrencyTest {
    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        int threads = 100;
        SynchronizedIncreaser increaser = new SynchronizedIncreaser();
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        for(int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    for(int j = 0; j < 100000; j ++){
                        increaser.increase();
                    }
                }finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        long end = System.currentTimeMillis();
        executor.shutdown();
        System.out.println("spend time: " + (end - start));
        System.out.println("result : " + increaser.getCounter());
    }
}
