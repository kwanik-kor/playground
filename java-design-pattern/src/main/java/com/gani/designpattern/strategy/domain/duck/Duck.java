package com.gani.designpattern.strategy.domain.duck;

import com.gani.designpattern.strategy.domain.behavior.FlyBehavior;
import com.gani.designpattern.strategy.domain.behavior.QuackBehavior;

public abstract class Duck {
    FlyBehavior flyBehavior;
    QuackBehavior quackBehavior;

    /**
     * 울기
     */
    public void quack() {
        quackBehavior.quack();
    }

    /**
     * 헤엄 헤엄
     */
    abstract void swim();

    /**
     * 우째 생겼습니까
     */
    abstract void display();

    /**
     * 날기
     */
    public void fly() {
        flyBehavior.fly();
    }

}
