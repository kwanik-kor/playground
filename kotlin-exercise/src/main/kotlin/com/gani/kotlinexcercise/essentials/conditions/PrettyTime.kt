package com.gani.kotlinexcercise.essentials.conditions

import org.junit.Test
import kotlin.test.assertEquals

/**
 * Converts a non-negative number of seconds into a human-readable string in hours, minutes, and seconds.
 *
 * Returns "Now" for zero seconds, and "Invalid input" for negative values.
 *
 * @param seconds The total number of seconds to convert.
 * @return A formatted string representing the equivalent time in hours, minutes, and seconds.
 */
fun secondsToPrettyTime(seconds: Int): String {
    val minute = 60
    val hour = minute * minute

    fun notDivisibleThenRecursive(seconds: Int, divider: Int) = if (seconds % divider == 0) "" else secondsToPrettyTime(seconds % divider)

    return when {
        seconds == 0 -> "Now"
        seconds >= hour -> "${seconds / hour} h " + notDivisibleThenRecursive(seconds, hour)
        seconds >= minute -> "${seconds / minute} min " + notDivisibleThenRecursive(seconds, minute)
        seconds < minute && seconds > 0-> "$seconds sec"
        else -> "Invalid input"
    }.trim()
}

/**
 * Demonstrates the usage of `secondsToPrettyTime` by printing formatted time strings for various input values.
 */
fun main() {
    println(secondsToPrettyTime(-1)) // Invalid input
    println(secondsToPrettyTime(0)) // Now
    println(secondsToPrettyTime(45)) // 45 sec
    println(secondsToPrettyTime(60)) // 1 min
    println(secondsToPrettyTime(150)) // 2 min 30 sec
    println(secondsToPrettyTime(1410)) // 23 min 30 sec
    println(secondsToPrettyTime(60 * 60)) // 1 h
    println(secondsToPrettyTime(3665)) // 1 h 1 min 5 sec
}

class PrettyTimeTest {

    @Test
    fun testNegativeSeconds() {
        val seconds = -1
        val expected = "Invalid input"
        assertEquals(expected, secondsToPrettyTime(seconds))
    }

    @Test
    fun testOnlySeconds() {
        val seconds = 45
        val expected = "45 sec"
        assertEquals(expected, secondsToPrettyTime(seconds))
    }

    @Test
    fun testOnlyMinutes() {
        val seconds = 60
        val expected = "1 min"
        assertEquals(expected, secondsToPrettyTime(seconds))
    }

    @Test
    fun testMinutesAndSeconds() {
        val seconds = 150
        val expected = "2 min 30 sec"
        assertEquals(expected, secondsToPrettyTime(seconds))
    }

    @Test
    fun testMinutesAndSecondsWithRemainder() {
        val seconds = 1410
        val expected = "23 min 30 sec"
        assertEquals(expected, secondsToPrettyTime(seconds))
    }

    @Test
    fun testOnlyHours() {
        val seconds = 3600
        val expected = "1 h"
        assertEquals(expected, secondsToPrettyTime(seconds))
    }

    @Test
    fun testHoursMinutesAndSeconds() {
        val seconds = 3665
        val expected = "1 h 1 min 5 sec"
        assertEquals(expected, secondsToPrettyTime(seconds))
    }

    @Test
    fun testZeroSeconds() {
        val seconds = 0
        val expected = "Now"
        assertEquals(expected, secondsToPrettyTime(seconds))
    }

    @Test
    fun testHoursMinutesSecondsWithZeroMinutes() {
        val seconds = 3605
        val expected = "1 h 5 sec"
        assertEquals(expected, secondsToPrettyTime(seconds))
    }

    @Test
    fun testHoursMinutesWithZeroSeconds() {
        val seconds = 7200
        val expected = "2 h"
        assertEquals(expected, secondsToPrettyTime(seconds))
    }

    @Test
    fun testMinutesSecondsWithZeroHours() {
        val seconds = 150
        val expected = "2 min 30 sec"
        assertEquals(expected, secondsToPrettyTime(seconds))
    }

    @Test
    fun testLargeValue() {
        val seconds = 123456789
        val expected = "34293 h 33 min 9 sec"
        assertEquals(expected, secondsToPrettyTime(seconds))
    }
}