package com.playground.elevatorsimulator.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playground.elevatorsimulator.Direction;
import com.playground.elevatorsimulator.Elevator;
import com.playground.elevatorsimulator.OutsideRequest;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class ElevatorWebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    private Elevator elevator;
    private Thread elevatorThread;

    @PostConstruct
    public void init() {
        // 엘리베이터를 랜덤 층에서 시작
        int initialFloor = new Random().nextInt(15) + 1;
        elevator = new Elevator(initialFloor);

        // 상태 변경 리스너 등록 - 상태가 변경되면 모든 클라이언트에게 브로드캐스트
        elevator.setStatusListener(this::broadcastElevatorStatus);

        // 엘리베이터 스레드 시작
        elevatorThread = new Thread(elevator, "Elevator-Thread");
        elevatorThread.start();

        log.info("Elevator initialized at floor {} and thread started", initialFloor);
    }

    @PreDestroy
    public void cleanup() {
        if (elevator != null) {
            elevator.shutdown();
        }
        if (elevatorThread != null) {
            try {
                elevatorThread.join(5000); // 최대 5초 대기
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Interrupted while waiting for elevator thread to stop", e);
            }
        }
        log.info("Elevator shutdown completed");
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        log.info("WebSocket connected: {}", session.getId());

        // 연결 확인 메시지 전송
        Map<String, Object> welcomeMessage = Map.of(
                "type", "CONNECTED",
                "message", "WebSocket connection established",
                "sessionId", session.getId()
        );
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(welcomeMessage)));

        // 현재 엘리베이터 상태 전송
        sendCurrentStatus(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("Received message from {}: {}", session.getId(), message.getPayload());

        // 메시지 파싱 및 처리
        Map<String, Object> payload = objectMapper.readValue(message.getPayload(), Map.class);
        String messageType = (String) payload.get("type");

        switch (messageType) {
            case "CALL_ELEVATOR":
                handleCallElevator(session, payload);
                break;
            case "REQUEST_STATUS":
                sendCurrentStatus(session);
                break;
            default:
                log.warn("Unknown message type: {}", messageType);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
        log.info("WebSocket disconnected: {}", session.getId());
    }

    private void handleCallElevator(WebSocketSession session, Map<String, Object> payload) throws IOException {
        Integer targetFloor = (Integer) payload.get("targetFloor");
        String directionStr = (String) payload.get("direction");

        Direction direction = Direction.valueOf(directionStr.toUpperCase());

        log.info("Elevator called from client {} - Floor: {}, Direction: {}",
                session.getId(), targetFloor, direction);

        // OutsideRequest 생성 및 엘리베이터 호출
        OutsideRequest request = new OutsideRequest(session.getId(), targetFloor, direction);
        elevator.callFromOutside(request);
    }

    private void sendCurrentStatus(WebSocketSession session) throws IOException {
        Map<String, Object> statusMap = createStatusMap(elevator.currentStatus());
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(statusMap)));
    }

    private void broadcastElevatorStatus(Elevator.ElevatorStatus status) {
        Map<String, Object> statusMap = createStatusMap(status);

        sessions.values().forEach(session -> {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(statusMap)));
                }
            } catch (IOException e) {
                log.error("Failed to send message to session {}", session.getId(), e);
            }
        });
    }

    private Map<String, Object> createStatusMap(Elevator.ElevatorStatus status) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "ELEVATOR_STATUS");
        map.put("currentFloor", status.currentFloor());
        map.put("direction", status.currentDirection() != null ? status.currentDirection().name() : null);
        map.put("isMoving", status.isMoving());
        map.put("doorsOpen", status.doorsOpen());
        map.put("upStops", status.upStops());
        map.put("downStops", status.downStops());
        return map;
    }
}
