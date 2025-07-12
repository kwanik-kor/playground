package com.gani.designpattern.strategy.domain.behavior;

public class FlyWithWings implements FlyBehavior {

    @Override
    public void fly() {
        System.out.println("날아올라");
    }

}
