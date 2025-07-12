package com.gani.designpattern.strategy.domain.behavior;

public class MuteQuack implements QuackBehavior {

    @Override
    public void quack() {
        System.out.println();
    }

    @Override
    public String toString() {
        return "MuteQuack(울수없음)";
    }

}
