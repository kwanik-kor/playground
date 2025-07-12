package com.gani.designpattern.strategy.domain.duck;

import com.gani.designpattern.strategy.domain.behavior.FlyWithWings;
import com.gani.designpattern.strategy.domain.behavior.Quack;

public class MallardDuck extends Duck {

    public MallardDuck() {
        this.flyBehavior = new FlyWithWings();
        this.quackBehavior = new Quack();
    }

    @Override
    void swim() {

    }

    @Override
    void display() {

    }

}
