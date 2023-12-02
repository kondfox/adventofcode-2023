package dec_02

import kotlin.math.max

/* Part 1 */

fun part1(input: List<String>): Int = input.mapIndexed { i, game ->
    if (countCubes(game).hasMax(red = 12, green = 13, blue = 14)) (i + 1) else 0
}.sum()

private fun countCubes(input: String): Map<String, Int> = input
    .split(": ")[1]
    .split("; ")
    .map(::toSamples)
    .mergeSamples()

private fun toSamples(samples: String): Map<String, Int> = samples
    .split(", ")
    .associate(::toCube)

private fun toCube(cube: String): Pair<String, Int> {
    val (count, color) = cube.split(" ")
    return Pair(color, count.toInt())
}

private fun List<Map<String, Int>>.mergeSamples(): Map<String, Int> = this
    .fold(mutableMapOf()) { mergedSamples, samples ->
        samples.forEach { (color, count) ->
            mergedSamples[color] = max(mergedSamples[color] ?: 0, count)
        }
        mergedSamples
    }

private fun Map<String, Int>.hasMax(red: Int, green: Int, blue: Int): Boolean =
    (this["red"] ?: 0) <= red && (this["green"] ?: 0) <= green && (this["blue"] ?: 0) <= blue

/* Part 2 */

fun part2(input: List<String>): Int = input.sumOf { game ->
    countCubes(game).power()
}

private fun Map<String, Int>.power(): Int = this.values.reduce(Int::times)