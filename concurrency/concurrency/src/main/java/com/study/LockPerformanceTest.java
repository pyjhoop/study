package com.study;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class LockPerformanceTest {
    
    // synchronized 버전
    static class SynchronizedCounter {
        private volatile int count = 0;
        
        public synchronized void increment() {
            count++;
        }
        
        public synchronized int getCount() {
            return count;
        }
    }
    
    // ReentrantLock 버전
    static class ReentrantLockCounter {
        private volatile int count = 0;
        private final ReentrantLock lock = new ReentrantLock();
        
        public void increment() {
            lock.lock();
            try {
                count++;
            } finally {
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
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Java 21 락 성능 비교 ===\n");
        
        int threadCount = 100;
        int operationsPerThread = 10000;
        
        // 1. synchronized 테스트
        long syncTime = testSynchronized(threadCount, operationsPerThread);
        
        // 2. ReentrantLock 테스트  
        long lockTime = testReentrantLock(threadCount, operationsPerThread);
        
        // 결과 비교
        System.out.println("\n=== 성능 비교 결과 ===");
        System.out.println("synchronized:  " + syncTime + "ms");
        System.out.println("ReentrantLock: " + lockTime + "ms");
        
        double improvement = ((double)(syncTime - lockTime) / syncTime) * 100;
        if (lockTime < syncTime) {
            System.out.printf("ReentrantLock이 %.1f%% 더 빠름!\n", improvement);
        } else {
            System.out.printf("synchronized가 %.1f%% 더 빠름\n", -improvement);
        }
        
        System.out.println("\n💡 성능 차이 이유:");
        System.out.println("1. ReentrantLock: CAS + Spin + 스마트한 대기");
        System.out.println("2. synchronized: JVM 모니터 락 + 블로킹");
        System.out.println("3. 경합이 많을수록 ReentrantLock 우위 증가");
    }
    
    private static long testSynchronized(int threadCount, int operations) 
            throws InterruptedException {
        System.out.println("🔒 synchronized 테스트 중...");
        
        SynchronizedCounter counter = new SynchronizedCounter();
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < operations; j++) {
                        counter.increment();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        long endTime = System.currentTimeMillis();
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        System.out.println("   결과: " + counter.getCount() + " (예상: " + (threadCount * operations) + ")");
        return endTime - startTime;
    }
    
    private static long testReentrantLock(int threadCount, int operations) 
            throws InterruptedException {
        System.out.println("🔐 ReentrantLock 테스트 중...");
        
        ReentrantLockCounter counter = new ReentrantLockCounter();
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < operations; j++) {
                        counter.increment();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        long endTime = System.currentTimeMillis();
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        System.out.println("   결과: " + counter.getCount() + " (예상: " + (threadCount * operations) + ")");
        return endTime - startTime;
    }
}
