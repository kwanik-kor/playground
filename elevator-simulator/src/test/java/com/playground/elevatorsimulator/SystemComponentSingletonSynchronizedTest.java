package com.playground.elevatorsimulator;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class SystemComponentSingletonSynchronizedTest {

    @Test
    void testSingletonInstanceAndSleepAndSayHello() throws InterruptedException {
        // Given: 여러 스레드를 준비
        int threadCount = 10;
        CountDownLatch latch = new CountDownLatch(threadCount);
        List<SystemComponentSingletonSynchronized.SystemComponent> instances = new ArrayList<>();

        // When: 여러 스레드에서 동시에 인스턴스 획득 및 sleepAndSayHello 호출
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    SystemComponentSingletonSynchronized.SystemComponent instance =
                        SystemComponentSingletonSynchronized.getInstance();

                    synchronized (instances) {
                        instances.add(instance);
                    }

                    instance.sleepAndSayHello();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            }).start();
        }

        latch.await();

        // Then: 모든 인스턴스가 동일한 객체인지 확인
        assertEquals(threadCount, instances.size());
        SystemComponentSingletonSynchronized.SystemComponent firstInstance = instances.get(0);

        for (SystemComponentSingletonSynchronized.SystemComponent instance : instances) {
            assertSame(firstInstance, instance, "모든 인스턴스는 동일한 싱글턴 객체여야 합니다");
        }


        Thread.sleep(10000);
    }

    @Test
    void testSingleInstanceCreation() throws InterruptedException {
        // Given & When: 인스턴스 획득
        SystemComponentSingletonSynchronized.SystemComponent instance1 =
            SystemComponentSingletonSynchronized.getInstance();
        SystemComponentSingletonSynchronized.SystemComponent instance2 =
            SystemComponentSingletonSynchronized.getInstance();

        // Then: 같은 인스턴스인지 확인
        assertSame(instance1, instance2, "getInstance()는 항상 같은 인스턴스를 반환해야 합니다");

        // sleepAndSayHello 호출
        instance1.sleepAndSayHello();
    }
}