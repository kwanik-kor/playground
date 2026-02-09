package com.gani.kotlinexcercise.essentials.sealed

class Malformed() : Result
class Jesus() : Result

// object declaration is possible
object Goat : Result

fun printTeaPot() = run {
    // Anonymous object cannot extend a sealed interface.
    val teaPot = object { //: Result {
        val status = 418
        val message = "I'm a teapot"
    }

    println("${teaPot.status} ${teaPot.message}")
}