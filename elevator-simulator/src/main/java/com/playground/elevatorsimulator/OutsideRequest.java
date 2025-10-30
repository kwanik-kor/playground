package com.playground.elevatorsimulator;

import lombok.Getter;

@Getter
public class OutsideRequest extends ElevatorRequest {
    private final Direction direction;

    public OutsideRequest(String user, int floor, Direction direction) {
        super(user, floor);
        this.direction = direction;
    }

}
