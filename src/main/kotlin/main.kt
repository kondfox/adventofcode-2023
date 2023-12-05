import dec_05.part1
import dec_05.part2
import java.io.File
import kotlin.time.measureTimedValue

fun main() {
    val day = "05"
    val example = File("src/main/kotlin/dec_$day/example.txt").readLines()
    val input = File("src/main/kotlin/dec_$day/input.txt").readLines()

    println("Part 1")
    println("* Example: ${solve { part1(example) }}")
    println("* Input: ${solve { part1(input) }}")

    println()

    println("Part 2")
    println("* Example: ${solve { part2(example) }}")
    println("* Input: ${solve { part2(input) }}")
}

fun solve(function: () -> Any): String {
    val (result, time) = measureTimedValue(function)
    return "$result [${time}]"
}
