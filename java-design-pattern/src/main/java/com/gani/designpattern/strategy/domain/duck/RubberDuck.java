package com.gani.designpattern.strategy.domain.duck;

import com.gani.designpattern.strategy.domain.behavior.FlyNoWay;
import com.gani.designpattern.strategy.domain.behavior.Squeak;

public class RubberDuck extends Duck {

    public RubberDuck() {
        this.flyBehavior = new FlyNoWay();
        this.quackBehavior = new Squeak();
    }

    @Override
    void swim() {

    }

    @Override
    void display() {

    }

}
