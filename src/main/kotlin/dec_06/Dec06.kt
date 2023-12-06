package dec_06

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

/* Part 1 */

fun part1(input: List<String>) = solve(parse1(input))

private fun parse1(input: List<String>) = parse(input)
    .map { it.map { it.value.toLong() }.toList() }.toList().let { (time, distance) -> time.zip(distance) }

private fun parse(input: List<String>) = input.windowed(2)
    .flatMap { it.map { it.split(":")[1].trim() } }
    .map { Regex("[0-9]+").findAll(it) }

private fun solve(races: List<Pair<Long, Long>>) = races
    .map { (time, distance) -> quadratic(1, -time, distance) }
    .map { ceil(it.second).toLong() - floor(it.first).toLong() - 1 }
    .reduce(Long::times)

private fun quadratic(a: Int, b: Long, c: Long): Pair<Double, Double> {
    val d = sqrt(b.toDouble() * b - 4 * a * c)
    return ((-b - d) / (2 * a)) to ((-b + d) / (2 * a))
}

/* Part 2 */

fun part2(input: List<String>) = solve(parse2(input))

private fun parse2(input: List<String>) = parse(input)
    .map { it.fold("") { num, n -> num + n.value }.toLong() }.zipWithNext()