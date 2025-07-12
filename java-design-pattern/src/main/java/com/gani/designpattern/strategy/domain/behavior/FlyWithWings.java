package com.gani.designpattern.strategy.domain.behavior;

public class FlyWithWings implements FlyBehavior {

    @Override
    public void fly() {
        System.out.println("날아올라");
    }

    @Override
    public String toString() {
        return "FlyWithWings(보편적인 날기)";
    }

}
