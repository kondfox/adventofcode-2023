package dec_01

import java.io.File

fun main() {
    File("src/main/kotlin/dec_01/input.txt")
        .readLines()
        .map { "${it.first(Character::isDigit)}${it.last(Character::isDigit)}".toInt() }
        .sum()
        .let(::println)
}