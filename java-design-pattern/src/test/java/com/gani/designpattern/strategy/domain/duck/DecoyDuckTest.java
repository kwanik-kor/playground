package com.gani.designpattern.strategy.domain.duck;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DecoyDuckTest {

    @Test
    void testDecoyDuckBehaviors() {
        DecoyDuck duck = new DecoyDuck();
        
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        
        duck.fly();
        duck.quack();
        
        String result = output.toString();
        assertEquals("날 수 없어\n\n", result);
        
        System.setOut(System.out);
    }
}