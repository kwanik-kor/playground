package com.gani.kotlinexcercise.essentials.functions

/**
 * WTF
 */
/**
 * Returns the input value unchanged.
 *
 * This generic function demonstrates the use of backticks to allow reserved keywords as identifiers.
 *
 * @return The same value as the input parameter.
 */
fun <Fun> `fun`(`fun`: Fun): Fun = `fun`

/**
 * Returns the string "void".
 *
 * This function demonstrates the use of backticks to allow reserved keywords as function names in Kotlin.
 */
fun `void`(): String {
    return "void"
}

/**
 * Returns the square of the given number.
 *
 * @param x The number to be squared.
 * @return The square of x.
 */
fun `단일표현식구문`(x: Double): Double = x * x

/**
 * Returns twice the value of the given integer.
 *
 * @param i The integer to double.
 * @return The result of multiplying the input by 2.
 */
fun double(i: Int) = i * 2
// 👆Top level functions

class A {
    /**
 * Returns three times the given integer.
 *
 * @param i The integer to be tripled.
 * @return The result of multiplying the input by 3.
 */
    private fun triple(i: Int) = i * 3

    /**
     * Returns twelve times the given integer.
     *
     * Multiplies the input by four using a local function, then triples the result.
     *
     * @param i The integer to be multiplied.
     * @return The value of `i` multiplied by 12.
     */
    fun twelveTimes(i: Int): Int {
        // Local function(Nested function)
        fun fourTimes() = double(double(i))
        return triple(fourTimes())
    }
}

/**
 * Prints the array of integer parameters provided as variable arguments.
 *
 * @param params The integers to print.
 */
fun temp(vararg params: Int) = println(params)

/**
 * Demonstrates joining a list of integers into a string with a custom separator and printing the result.
 */
fun main() {
    val list = listOf(1, 2, 3, 4)
    println(list.joinToString(separator = "-"))
}

/**
 * Prints the result of a bitwise AND operation between two binary literals using infix notation.
 */
fun bitCalculator() {
    println(0b011 and 0b001)
}