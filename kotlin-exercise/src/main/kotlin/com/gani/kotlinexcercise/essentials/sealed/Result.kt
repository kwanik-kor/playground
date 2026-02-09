package com.gani.kotlinexcercise.essentials.sealed

sealed interface Result

sealed class Foo

class Success(val data: String) : Result
class Failure(val exception: Throwable) : Result

fun handleResult(result: Result) = when (result) {
    is Success -> println("성공 히히힝")
    is Failure -> println("-,.-")
    else -> error("oh oh don't do that")
}
