package com.playground.elevatorsimulator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SystemComponentSingletonSynchronized {
    private static SystemComponent instance;
    private SystemComponentSingletonSynchronized() {}

    public static synchronized SystemComponent getInstance() {
        log.info("{} thread: Trying to get singleton", Thread.currentThread().getName());
        if(instance == null) {
            instance = new SystemComponent();
        }
        return instance;
    }

    public static class SystemComponent {
        private SystemComponent() {}

        public void sleepAndSayHello() throws InterruptedException {
            Thread.sleep(500);
            log.info("Hello! {} thread", Thread.currentThread().getName());
        }
    }
}
