package com.gani.designpattern.strategy.domain;

public class RubberDuck extends Duck{

    @Override
    void quack() {
        System.out.println("삑삑");
    }

    @Override
    void swim() {

    }

    @Override
    void display() {

    }

    @Override
    void fly() {
        // Do nothing
    }

}
