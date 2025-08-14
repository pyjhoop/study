package com.study.problem;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrencyTest {
    
    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        Increaser increaser = new Increaser();
        ExecutorService executor = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(100);

        // 모든 작업을 한번에 제출 (더 동시성 높음)
        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < 100000; j++) {
                        increaser.increase();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 작업 완료 대기
        long endTime = System.currentTimeMillis();

        executor.shutdown();
        System.out.println("spend time : " + (endTime - startTime));
        System.out.println("result: " + increaser.getCount());
    }
}
