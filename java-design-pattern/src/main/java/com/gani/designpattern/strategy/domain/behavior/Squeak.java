package com.gani.designpattern.strategy.domain.behavior;

public class Squeak implements QuackBehavior {

    @Override
    public void quack() {
        System.out.println("삑");
    }

}
