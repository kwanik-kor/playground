package com.gani.kotlinexcercise.essentials.classes

import java.util.Date

/**
 * Kotlin 에서는 프로퍼티 방식을 지원하며, val 은 getter를 var은 getter, setter를 지원
 */
class User {
    var name: String = ""
        get() = field.uppercase() // Custom getter
        set(value) { // Custom setter
            if (value.isNotBlank()) {
                field = value
            }
        }

    var surname: String = ""
    val fullName: String // field 키워드를 사용하지 않으면 필드가 생성되지 않음
        get() = "$name $surname"
    var birthDateMillis: Long? = null
    var birthdate: Date?
        get() {
            val millis = birthDateMillis
            return if (millis == null) null else Date(millis)
        }
        set(value) {
            birthDateMillis  = value?.time
        }
}

fun main(args: Array<String>) {
    val user = User()
    user.name = "Alex" // setter 호출
    user.surname = "Ferguson"
    println(user.name)

    println(user.fullName)
}

class Puppy(val name: String?= "이름 없음") {

    class InnerStaticPuppy {
        fun think() {}
    }

    inner class InnerPuppy {
        fun think() {}
    }

}

fun innerTest() {
    val innerStaticPuppy = Puppy.InnerStaticPuppy()
    val innerPuppy = Puppy().InnerPuppy()
}