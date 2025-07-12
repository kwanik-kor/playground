package com.gani.designpattern.strategy.domain.behavior;

public class FlyNoWay implements FlyBehavior {

    @Override
    public void fly() {
        System.out.println("날 수 없어");
    }

    @Override
    public String toString() {
        return "FlyNoWay(무늬만 날개)";
    }

}
