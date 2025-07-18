package com.gani.kotlinexcercise.essentials.conditions

/**
 * 코틀린의 if-else는 표현식으로 사용할 수 있으므로 값을 생성할 수 있음<br>
 * - 삼항연산자를 대신해서 사용함<br>
 *   - 엘비스 연산자, null 가능한 타입의 확장 함수, 안전 호출과 같이 삼항 연산자를 대체할 수 있는 수단이 많음
 */
fun conditionAsVariable() {
    val condition = true
    val value = if (condition) 1 else 0
}

/**
 * 반환값이 없거나, 표현식이 아닌 것은 Unit을 반환함
 */
fun seemsNothingReturn(): Unit {
}

/**
 * if-else-if는 if-else 안에 if-else가 중첩되어 있을뿐!
 */
fun `if-else-if is actually if-else`() {

    fun Any?.print() {
        print(this)
    }

    fun printNumberSign(num: Int) {
        if (num < 0) "negative"
        else if (num > 0) "positive"
        else "zero" .print()
    }

}

/**
 * as로 다운캐스팅 및 스마트 캐스팅을 할 수 있지만, Code smell
 */
fun casting() {

    fun downCasting(i: Number) {
        val j = (i as Int) + 10
    }

    fun smartCasting(i: Number) {
        val j = i.toInt() + 10
    }

    fun smartCasting2(i: Number) {
        val j: Int? = i as? Int
    }
}

