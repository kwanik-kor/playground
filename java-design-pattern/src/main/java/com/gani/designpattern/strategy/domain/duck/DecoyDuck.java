package com.gani.designpattern.strategy.domain.duck;

import com.gani.designpattern.strategy.domain.behavior.FlyNoWay;
import com.gani.designpattern.strategy.domain.behavior.MuteQuack;

public class DecoyDuck extends Duck {

    public DecoyDuck() {
        this.flyBehavior = new FlyNoWay();
        this.quackBehavior = new MuteQuack();
    }

    @Override
    void swim() {

    }

    @Override
    void display() {

    }

}
