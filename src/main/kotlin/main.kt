import dec_05.part1
import dec_05.part2
import java.io.File

fun main() {
    val day = "05"
    val example = File("src/main/kotlin/dec_$day/example.txt").readLines()
    val input = File("src/main/kotlin/dec_$day/input.txt").readLines()

    println("Part 1")
    println("* Example: ${part1(example)}")
    println("* Input: ${part1(input)}")

    println()

    println("Part 2")
    println("* Example: ${part2(example)}")
    println("* Input: ${part2(input)}")
}