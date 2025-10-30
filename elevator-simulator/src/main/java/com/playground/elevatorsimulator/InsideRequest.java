package com.playground.elevatorsimulator;

public class InsideRequest extends ElevatorRequest {
    private final int destinationFloor;

    public InsideRequest(String user, int floor, int destinationFloor) {
        super(user, floor);
        this.destinationFloor = destinationFloor;
    }

}
