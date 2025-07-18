package com.gani.kotlinexcercise.essentials.conditions

/**
 * 코틀린의 if-else는 표현식으로 사용할 수 있으므로 값을 생성할 수 있음<br>
 * - 삼항연산자를 대신해서 사용함<br>
 *   - 엘비스 연산자, null 가능한 타입의 확장 함수, 안전 호출과 같이 삼항 연산자를 대체할 수 있는 수단이 많음
 */
/**
 * Demonstrates using an if-else expression to assign a value based on a condition.
 *
 * Shows how Kotlin's if-else can be used as an expression to replace the ternary operator found in other languages.
 */
fun conditionAsVariable() {
    val condition = true
    val value = if (condition) 1 else 0
}

/**
 * Demonstrates that a function with no return value or expression in Kotlin returns `Unit`.
 */
fun seemsNothingReturn(): Unit {
}

/**
 * Demonstrates that `if-else-if` chains are nested `if-else` statements by using conditional logic to determine the sign of a number.
 *
 * Contains a nested function that prints "zero" if the input is zero; otherwise, it evaluates to "negative" or "positive" without printing.
 */
fun `if-else-if is actually if-else`() {

    /**
     * Prints the string representation of the object to the standard output.
     */
    fun Any?.print() {
        print(this)
    }

    /**
     * Prints "zero" if the given number is zero; otherwise, does nothing.
     *
     * @param num The integer to check for sign.
     */
    fun printNumberSign(num: Int) {
        if (num < 0) "negative"
        else if (num > 0) "positive"
        else "zero" .print()
    }

}

/**
 * Demonstrates different approaches to type casting in Kotlin, including forced downcasting, smart casting using conversion, and safe casting.
 *
 * Contains examples of:
 * - Forced downcasting with `as`, which can throw an exception if the type is incorrect.
 * - Smart casting by converting a `Number` to `Int` using `toInt()`.
 * - Safe casting with `as?`, which returns `null` if the cast is not possible.
 */
fun casting() {

    /**
     * Performs a forced cast of the given number to an `Int` and adds 10.
     *
     * If `i` is not an `Int`, a `ClassCastException` will be thrown.
     */
    fun downCasting(i: Number) {
        val j = (i as Int) + 10
    }

    /**
     * Converts the given number to an integer, adds 10, and assigns the result to a local variable.
     *
     * This function demonstrates smart casting using the `toInt()` method on a `Number`.
     */
    fun smartCasting(i: Number) {
        val j = i.toInt() + 10
    }

    /**
     * Attempts to safely cast the given number to an `Int`, assigning the result to a nullable variable.
     *
     * If `i` is not an `Int`, the result will be `null`.
     */
    fun smartCasting2(i: Number) {
        val j: Int? = i as? Int
    }
}

