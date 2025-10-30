package com.playground.elevatorsimulator;

import lombok.Getter;

@Getter
public abstract class ElevatorRequest {
    protected String user;
    protected int floor;
    protected long timestamp;

    public ElevatorRequest(String user, int floor) {
        this.user = user;
        this.floor = floor;
        this.timestamp = System.currentTimeMillis();
    }
}
