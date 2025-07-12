package com.gani.designpattern.strategy.domain.behavior;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FlyBehaviorTest {

    static Stream<Arguments> flyBehaviorProvider() {
        return Stream.of(
                Arguments.of(new FlyWithWings(), "날아올라\n"),
                Arguments.of(new FlyNoWay(), "날 수 없어\n")
        );
    }

    @ParameterizedTest(name = "{0} 나는 방법 = {1}")
    @MethodSource("flyBehaviorProvider")
    void 울음소리_전략은_각각의_소리를_낸다(FlyBehavior flyBehavior, String expected) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        flyBehavior.fly();
        assertEquals(expected, output.toString());

        System.setOut(System.out);
    }

}