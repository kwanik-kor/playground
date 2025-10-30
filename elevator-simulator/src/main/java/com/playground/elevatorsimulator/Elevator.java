package com.playground.elevatorsimulator;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

@Slf4j
@Getter
public class Elevator implements Runnable {

    private static final int MIN_FLOOR = 1;
    private static final int MAX_FLOOR = 15;
    private static final int MOVE_DELAY = 1000; // 1초 - 한 층 이동 시간
    private static final int STOP_DELAY = 2000; // 2초 - 중간 정지 시간

    private final Lock lock = new ReentrantLock();
    private final Condition hasRequest = lock.newCondition();

    private int currentFloor;
    private Direction currentDirection;
    private boolean isMoving;
    private boolean doorsOpen;
    private final Set<Integer> upStops = new TreeSet<>(); // 올라가면서 멈출 층들
    private final Set<Integer> downStops = new TreeSet<>(Collections.reverseOrder()); // 내려가면서 멈출 층들

    private volatile boolean running = true;
    private Consumer<ElevatorStatus> statusListener;

    public Elevator(int initialFloor) {
        if (initialFloor < MIN_FLOOR || initialFloor > MAX_FLOOR) {
            throw new IllegalArgumentException("Initial floor must be between " + MIN_FLOOR + " and " + MAX_FLOOR);
        }
        this.currentFloor = initialFloor;
        this.currentDirection = null;
        this.isMoving = false;
        this.doorsOpen = false;
    }

    public void setStatusListener(Consumer<ElevatorStatus> listener) {
        this.statusListener = listener;
    }

    /**
     * 외부에서 엘리베이터 호출 (OutsideRequest)
     */
    public void callFromOutside(OutsideRequest request) {
        lock.lock();
        try {
            int floor = request.getFloor();

            // 유효한 층수 범위 체크
            if (floor < MIN_FLOOR || floor > MAX_FLOOR) {
                log.warn("Invalid floor request: {}. Must be between {} and {}",
                        floor, MIN_FLOOR, MAX_FLOOR);
                return;
            }

            // 같은 층이면 문만 열고 닫기
            if (floor == currentFloor) {
                log.info("Already at floor {}, opening doors", floor);
                openAndCloseDoors();
                return;
            }

            log.info("Outside request received - Floor: {}, User wants to go: {}",
                    floor, request.getDirection());

            // 엘리베이터가 호출된 층으로 가기 위한 방향 결정
            // 호출된 층이 현재 층보다 위에 있으면 UP, 아래에 있으면 DOWN
            if (floor > currentFloor) {
                upStops.add(floor);
                log.info("Added floor {} to UP stops (current floor: {})", floor, currentFloor);
            } else {
                downStops.add(floor);
                log.info("Added floor {} to DOWN stops (current floor: {})", floor, currentFloor);
            }

            hasRequest.signal();
            notifyStatus();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        log.info("Elevator started at floor {}", currentFloor);
        notifyStatus();

        while (running) {
            lock.lock();
            try {
                // 요청이 없으면 대기
                while (upStops.isEmpty() && downStops.isEmpty() && running) {
                    hasRequest.await();
                }

                if (!running) break;

                // 현재 방향 결정
                determineDirection();

                // 이동 수행
                if (currentDirection != null) {
                    processMovement();
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Elevator thread interrupted", e);
                break;
            } finally {
                lock.unlock();
            }
        }

        log.info("Elevator stopped");
    }

    /**
     * 엘리베이터 이동 방향 결정
     */
    private void determineDirection() {
        if (currentDirection == null) {
            // 현재 멈춰있는 상태 - 가까운 요청 선택
            if (!upStops.isEmpty()) {
                currentDirection = Direction.UP;
            } else if (!downStops.isEmpty()) {
                currentDirection = Direction.DOWN;
            }
        }

        // 현재 방향의 요청이 없으면 방향 전환
        if (currentDirection == Direction.UP && upStops.isEmpty() && !downStops.isEmpty()) {
            currentDirection = Direction.DOWN;
        } else if (currentDirection == Direction.DOWN && downStops.isEmpty() && !upStops.isEmpty()) {
            currentDirection = Direction.UP;
        }
    }

    /**
     * 엘리베이터 이동 처리
     */
    private void processMovement() throws InterruptedException {
        Set<Integer> targetStops = (currentDirection == Direction.UP) ? upStops : downStops;

        while (!targetStops.isEmpty()) {
            Integer nextStop = targetStops.iterator().next();

            // 유효하지 않은 층은 제거
            if (nextStop < MIN_FLOOR || nextStop > MAX_FLOOR) {
                log.warn("Invalid floor requested: {}, removing from queue", nextStop);
                targetStops.remove(nextStop);
                continue;
            }

            // 목표 층까지 한 층씩 이동
            while (currentFloor != nextStop) {
                isMoving = true;
                notifyStatus();

                // 1층씩 이동 (경계 체크 포함)
                if (currentDirection == Direction.UP && currentFloor < MAX_FLOOR) {
                    currentFloor++;
                } else if (currentDirection == Direction.DOWN && currentFloor > MIN_FLOOR) {
                    currentFloor--;
                } else {
                    // 더 이상 이동할 수 없으면 중단
                    log.warn("Cannot move {} from floor {}", currentDirection, currentFloor);
                    break;
                }

                log.info("Moving {} - Current floor: {}", currentDirection, currentFloor);

                // Lock을 해제하고 1초 대기 (한 층 이동 시간)
                lock.unlock();
                try {
                    Thread.sleep(MOVE_DELAY);
                } finally {
                    lock.lock();
                }

                notifyStatus();

                // 현재 층에 중간 정지 요청이 있는지 확인
                if (targetStops.contains(currentFloor) && currentFloor != nextStop) {
                    log.info("Intermediate stop at floor {}", currentFloor);
                    isMoving = false;
                    targetStops.remove(currentFloor);

                    // 문 열고 닫기 (2초)
                    lock.unlock();
                    try {
                        openAndCloseDoors();
                    } finally {
                        lock.lock();
                    }
                }
            }

            // 목표 층 도착
            isMoving = false;
            targetStops.remove(nextStop);

            // 문 열고 닫기 (2초)
            lock.unlock();
            try {
                openAndCloseDoors();
            } finally {
                lock.lock();
            }
        }

        // 모든 요청 처리 완료
        currentDirection = null;
        isMoving = false;
        notifyStatus();
    }

    /**
     * 문 열고 2초 후 닫기 (같은 층 요청 또는 승하차용)
     */
    private void openAndCloseDoors() {
        lock.lock();
        try {
            doorsOpen = true;
            log.info("Doors opening at floor {}", currentFloor);
            notifyStatus();

            // 2초 동안 문 열림
            lock.unlock();
            try {
                Thread.sleep(STOP_DELAY);
            } finally {
                lock.lock();
            }

            doorsOpen = false;
            log.info("Doors closing at floor {}", currentFloor);
            notifyStatus();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted while doors were open", e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 상태 변경 알림
     */
    private void notifyStatus() {
        if (statusListener != null) {
            ElevatorStatus status = new ElevatorStatus(
                    currentFloor,
                    currentDirection,
                    isMoving,
                    doorsOpen,
                    new ArrayList<>(upStops),
                    new ArrayList<>(downStops)
            );
            statusListener.accept(status);
        }
    }

    public void shutdown() {
        lock.lock();
        try {
            running = false;
            hasRequest.signal();
        } finally {
            lock.unlock();
        }
    }

    public ElevatorStatus currentStatus() {
        return new ElevatorStatus(
                currentFloor,
                currentDirection,
                isMoving,
                isDoorsOpen(),
                upStops.stream().toList(),
                downStops.stream().toList()
        );
    }

    /**
     * 엘리베이터 상태 정보
     */
    public record ElevatorStatus(
            int currentFloor,
            Direction currentDirection,
            boolean isMoving,
            boolean doorsOpen,
            List<Integer> upStops,
            List<Integer> downStops
    ) {}
}
