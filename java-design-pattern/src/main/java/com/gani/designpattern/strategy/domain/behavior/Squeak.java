package com.gani.designpattern.strategy.domain.behavior;

public class Squeak implements QuackBehavior {

    @Override
    public void quack() {
        System.out.println("삑");
    }

    @Override
    public String toString() {
        return "Squeak(장난감 울음소리)";
    }
}
