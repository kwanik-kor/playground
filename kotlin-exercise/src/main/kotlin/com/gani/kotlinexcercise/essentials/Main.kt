@file:JvmName("Main")

package com.gani.kotlinexcercise.essentials

/**
 * 코틀린은 어떻게 클래스 없이 가능했던 거지?<br>
 * 는 ByteCode를 살펴보면 MainKt 형태의 클래스를 만들기 때문!
 */
fun main() {
    println("Hello Kotlin!!")
}

/**
 * JvmName으로 컴파일되는 파일 명을 강제로 변경할 수 있지만, 동일한 패키지 내에 동일한 클래스 며잉 사용된다면 컴파일 에러가 발생하게 됨!<br>
 * Duplicate JVM class name 'com/gani/kotlinexcercise/essentials/Main' generated from: Main, Main
 */
//class Main {
//    fun sayHello() {
//        println("Hello")
//    }
//}