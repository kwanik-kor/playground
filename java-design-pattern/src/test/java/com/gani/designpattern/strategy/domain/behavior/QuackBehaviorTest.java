package com.gani.designpattern.strategy.domain.behavior;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuackBehaviorTest {

    static Stream<Arguments> quackBehaviorProvider() {
        return Stream.of(
                Arguments.of(new Quack(), "꽥\n"),
                Arguments.of(new Squeak(), "삑\n"),
                Arguments.of(new MuteQuack(), "\n")
        );
    }

    @ParameterizedTest(name = "{0} 울음소리 = {1}")
    @MethodSource("quackBehaviorProvider")
    void 울음소리_전략은_각각의_소리를_낸다(QuackBehavior quackBehavior, String expected) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        quackBehavior.quack();
        assertEquals(expected, output.toString());

        System.setOut(System.out);
    }

}