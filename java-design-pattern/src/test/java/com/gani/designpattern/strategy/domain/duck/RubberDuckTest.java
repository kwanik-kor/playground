package com.gani.designpattern.strategy.domain.duck;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RubberDuckTest {

    @Test
    void testRubberDuckBehaviors() {
        RubberDuck duck = new RubberDuck();
        
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        
        duck.fly();
        duck.quack();
        
        String result = output.toString();
        assertEquals("날 수 없어\n삑\n", result);
        
        System.setOut(System.out);
    }
}