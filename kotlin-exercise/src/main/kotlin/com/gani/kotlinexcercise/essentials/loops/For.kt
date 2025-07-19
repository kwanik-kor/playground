package com.gani.kotlinexcercise.essentials.loops

/**
 * Iterable 인터페이스 구현
 */
class Tree(
    val value: String,
    val left: Tree? = null,
    val right: Tree? = null,
) : Iterable<String> {

    override fun iterator(): Iterator<String> = iterator {
        if (left != null) yieldAll(left)
        yield(value)
        if (right != null) yieldAll(right)
    }

}

/**
 * Range
 */
fun intRange() {
    // closed range
    for (i in 1..5) {
        print(i)
    }

    // opened range
    for (i in 1 ..< 5) {
        print(i)
    }

    for (i in 1 until 5) {
        print(i)
    }

    // descend
    for (i in 5 downTo 1) {
        print(i)
    }
}

// Sexy ways
fun main() {
    val names = listOf("Alex", "Bob", "Celina")

    for (i in names.indices) {
        val name = names[i]
    }

    for ((i, name) in names.withIndex()) {
        println("[$i] $name")
    }

    names.forEachIndexed { i, name ->
        println("[$i] $name")
    }

    val capitals = mapOf(
        "Korea" to "Seoul",
        "USA" to "Washington DC",
    )

    for ((country, capital) in capitals) {
        println("The capital of $country is $capital")
    }

    capitals.forEach { (country, capital) ->
        println("The capital of $country is $capital")
    }
}