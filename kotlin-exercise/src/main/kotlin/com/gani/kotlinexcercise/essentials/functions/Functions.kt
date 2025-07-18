package com.gani.kotlinexcercise.essentials.functions

/**
 * WTF
 */
fun <Fun> `fun`(`fun`: Fun): Fun = `fun`

/**
 * 예약어를 백틱으로 작성한 함수는 어떻게 컴파일 될까? <br>
 * 문자열로 인식시키기 때문에 컴파일에는 문제가 없음! JVM은 바이트코드만 이해하므로 전혀 상관없음.
 * 단, Java는 문법 파싱 과정에서의 코드 해석 토큰 추돌을 방지하기 위해 예약어 사용 X
 */
fun `void`(): String {
    return "void"
}

fun `단일표현식구문`(x: Double): Double = x * x

fun double(i: Int) = i * 2
// 👆Top level functions

class A {
    // Member function
    private fun triple(i: Int) = i * 3

    fun twelveTimes(i: Int): Int {
        // Local function(Nested function)
        fun fourTimes() = double(double(i))
        return triple(fourTimes())
    }
}

// vararg
fun temp(vararg params: Int) = println(params)

/**
 * 선택적 매개변수
 * 도메인 유효성 검사에서 선택적 매개변수를 사용해서 복수의 생성자를 안만들어도 될까?
 * - 난 좀 반댈세. 물론 Default를 정해놓겠지만 도메인 객체에 너무 값을 유동적으로 주는 것은 변수가 많아지지 않는가..?
 */
fun main() {
    val list = listOf(1, 2, 3, 4)
    println(list.joinToString(separator = "-"))
}

/**
 * 중위(infix) 표기법
 */
fun bitCalculator() {
    println(0b011 and 0b001)
}