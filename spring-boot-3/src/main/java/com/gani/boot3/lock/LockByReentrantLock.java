package com.gani.boot3.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockByReentrantLock {
    private final Lock lock = new ReentrantLock();
    private final Semaphore semaphore = new Semaphore(2);
    private final AtomicInteger atomicInteger = new AtomicInteger(0);
    private final Map<String, String> sessions = new HashMap<>();

    public void addSession(String key, String session) {
        lock.lock();

        try {
            sessions.put(key, session);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 동시 요청에 의해 HashMap에 값이 추가되지 않을 수 있는 메서드
     */
//    public void addSession(String key, String session) {
//        sessions.put(key, session);
//    }

    public String getSession(String key) {
        lock.lock();
        try {
            return sessions.get(key);
        } finally {
            lock.unlock();
        }
    }

}
