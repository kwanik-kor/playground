package com.gani.boot3.lock;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LockByReentrantLockTest {

    private static final Logger log = LoggerFactory.getLogger(LockByReentrantLockTest.class);

    @Test
    void concurrentTest() {
        ExecutorService executor = Executors.newFixedThreadPool(500);
        LockByReentrantLock locker = new LockByReentrantLock();

        List<Future<?>> futures = new ArrayList<>();

        int sessionCount = 1_000;
        for (int i = 1; i <= sessionCount; i++) {
            String sessionId = "session-" + i;
            Future<?> future = executor.submit(() -> {
                locker.addSession(sessionId, sessionId);
            });
            futures.add(future);
        }

        futures.forEach(f -> {
            try {
                f.get();
            } catch(Exception e) {
                log.error(e.getMessage(), e);
            }
        });

        executor.shutdown();

        for (int i = 1; i <= sessionCount; i++) {
            String sessionId = "session-" + i;
            String session = locker.getSession(sessionId);
            assertThat(session)
                    .describedAs("session %s", sessionId)
                    .isNotNull();
        }
    }
}