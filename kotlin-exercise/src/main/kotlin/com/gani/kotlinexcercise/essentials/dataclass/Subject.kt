package com.gani.kotlinexcercise.essentials.dataclass

data class Subject(
    val name: String,
    val capacity: Int,
) {
    var description: String?= null

}

fun main() {
    val hell = Subject("네덜란드 문학의 경계를 넘어서", 20)
    val anotherHell = hell.copy(name = "네덜란드 정치")
}