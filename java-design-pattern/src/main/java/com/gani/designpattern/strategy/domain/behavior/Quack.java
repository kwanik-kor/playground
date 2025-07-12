package com.gani.designpattern.strategy.domain.behavior;

public class Quack implements QuackBehavior {

    @Override
    public void quack() {
        System.out.println("꽥");
    }

}
