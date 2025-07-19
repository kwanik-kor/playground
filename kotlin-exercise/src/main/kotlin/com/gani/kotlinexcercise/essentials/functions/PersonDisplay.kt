package com.gani.kotlinexcercise.essentials.functions

import org.junit.Test
import kotlin.test.assertEquals

/**
 * Formats a person's name, surname, and age into a single display string.
 *
 * Constructs the string by appending the non-null parameters in order: name, surname (preceded by a space), and age (enclosed in parentheses and preceded by a space). The result is trimmed of leading and trailing whitespace.
 *
 * @param name The person's first name, or null to omit.
 * @param surname The person's surname, or null to omit.
 * @param age The person's age, or null to omit.
 * @return The formatted display string containing the provided information.
 */
fun formatPersonDisplay(
    name: String? = null,
    surname: String? = null,
    age: Int? = null,
): String {
    var result = ""

    if (name != null) result += name
    if (surname != null) result += " $surname"
    if (age != null) result += " ($age)"

    return result.trim()
}

/**
 * Demonstrates the usage of the `formatPersonDisplay` function with various combinations of name, surname, and age.
 *
 * Prints formatted person display strings to standard output.
 */
fun main() {
    println(formatPersonDisplay("John", "Smith", 42))
    // John Smith (42)
    println(formatPersonDisplay("Alex", "Simonson"))
    // Alex Simonson
    println(formatPersonDisplay("Peter", age = 25))
    // Peter (25)
    println(formatPersonDisplay(surname="Johnson", age=18))
    // Johnson (18)
}

class PersonDisplayTest {
    @Test
    fun testFormatPersonDisplay() {
        val name = "John"
        val surname = "Smith"
        val age = 42
        val expected = "John Smith (42)"
        assertEquals(expected, formatPersonDisplay(name, surname, age))
    }

    @Test
    fun testFormatPersonDisplayWithoutAge() {
        val name = "Alex"
        val surname = "Simonson"
        val expected = "Alex Simonson"
        assertEquals(expected, formatPersonDisplay(name, surname))
    }

    @Test
    fun testFormatPersonDisplayWithoutSurname() {
        val name = "Peter"
        val age = 25
        val expected = "Peter (25)"
        assertEquals(expected, formatPersonDisplay(name = name, age = age))
    }

    @Test
    fun testFormatPersonDisplayWithoutName() {
        val surname = "Johnson"
        val age = 18
        val expected = "Johnson (18)"
        assertEquals(expected, formatPersonDisplay(surname = surname, age = age))
    }

    @Test
    fun testFormatPersonDisplayWithoutNameAndSurname() {
        val age = 18
        val expected = "(18)"
        assertEquals(expected, formatPersonDisplay(age = age))
    }

    @Test
    fun testFormatPersonDisplayWithoutParameters() {
        val expected = ""
        assertEquals(expected, formatPersonDisplay())
    }

    @Test
    fun testFormatPersonDisplayWithNullName() {
        val name: String? = null
        val surname = "Smith"
        val age = 42
        val expected = "Smith (42)"
        assertEquals(expected, formatPersonDisplay(name, surname, age))
    }

    @Test
    fun testFormatPersonDisplayWithNullSurname() {
        val name = "John"
        val surname: String? = null
        val age = 42
        val expected = "John (42)"
        assertEquals(expected, formatPersonDisplay(name, surname, age))
    }
}