package com.gani.kotlinexcercise.essentials.`object`

class Pizza(
    val toppings: List<String>,
) {
    companion object {
        fun hawaiian(): Pizza = Pizza(listOf("pineapple", "pineapple", "pineapple"))
        fun margherita(): Pizza = Pizza(listOf("cheese", "arugula", "tomato"))
    }
}

fun main() {
    val hawaiian = Pizza.hawaiian()
    println(hawaiian.toppings)

    val margherita = Pizza.margherita()
    println(margherita.toppings)
}