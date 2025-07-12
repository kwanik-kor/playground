package com.gani.designpattern.strategy.domain.duck;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MallardDuckTest {

    @Test
    void testMallardDuckBehaviors() {
        MallardDuck duck = new MallardDuck();
        
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        
        duck.fly();
        duck.quack();
        
        String result = output.toString();
        assertEquals("날아올라\n꽥\n", result);
        
        System.setOut(System.out);
    }
}