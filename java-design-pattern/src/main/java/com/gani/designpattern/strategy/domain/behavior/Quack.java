package com.gani.designpattern.strategy.domain.behavior;

public class Quack implements QuackBehavior {

    @Override
    public void quack() {
        System.out.println("꽥");
    }

    @Override
    public String toString() {
        return "Quack(보편적인 울음소리)";
    }
}
