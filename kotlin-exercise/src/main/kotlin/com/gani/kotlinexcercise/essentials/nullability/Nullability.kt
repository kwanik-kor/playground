package com.gani.kotlinexcercise.essentials.nullability

/*
 * Let's be friend with NULL
 * 1. 모든 프로퍼티에 값을 할당해야 함(묵시적 null 할당 no thanks)
 * 2. 일반 타입은 null을 가질 수 없음
 * 3. 널 가능한 타입으로 하려면 일반 타입 끝에 물음표를 붙여야 함
 * 4. 널 가능한 타입을 직접 사용할 수 없음
 */

/**
 * SAFE CALL(?.)
 */
class TempUser(val name: String) {
    fun cheer() {
        println("취얼업 베이베 취얼업 베이베 좀 더 힘을 내")
    }
}

var user: TempUser? = null

fun safeCall() {
    user?.cheer() // Do nothing
    println(user?.name)
    user = TempUser("취얼업 베이베")
    user?.cheer()
    println(user?.name)
}

/**
 * NOT-NULL ASSERTION <br>
 * 아 null 아니라고!
 */
var user2: TempUser? = TempUser("Cookie")

fun assertion() {
    println(user2!!.name.length)
    val name = requireNotNull(user2!!.name) // If null throws IllegalArgumentException
    val name2 = checkNotNull(user2!!.name) // If null throws IllegalStateException
}

/**
 * SMART CASTING <br>
 * 스마트 캐스팅이 가능한 것은 코틀린의 Contracts 덕분!!
 */
fun printLengthOfNotNull(str: String?) {
    if (str == null) return
    println(str.length) // String으로 스마트 캐스팅
}

/**
 * ELVIS OPERATOR <br>
 * 널 가능한 값의 기본값을 제공하기 위함
 */
fun printName(user: User?) {
    println(user?.name ?: "이름없음")
}

/**
 * LATEINIT(지연 초기화)
 * - null이 가능하지 않지만, 인스턴스 생성 과정에서는 초기화할 수 없음
 */
lateinit var text: String

fun lateinitTest() {
    println(text) // Runtime Error
    if (::text.isInitialized) {
        println(text)
    }
}