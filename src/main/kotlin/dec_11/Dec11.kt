package dec_11

import kotlin.math.abs

/* Part 1 */

fun part1(input: List<String>): Int {
    val galaxies = expand(input, 2)
    val pairs = createPairs(galaxies)
    return pairs.sumOf { abs(it.first.first - it.second.first) + abs(it.first.second - it.second.second) }
}

private fun expand(universe: List<String>, expansion: Int = 2): List<Pair<Int, Int>> {
    val expandedRows = universe.indices.filter { y -> universe[y].all { it == '.' } }
    val expandedCols = universe[0].indices.filter { x -> universe.map { it[x] }.all { it == '.' } }
    return parse(universe)
        .map { galaxy ->
            val yMod = expandedRows.count { galaxy.first > it } * (expansion - 1)
            val xMod = expandedCols.count { galaxy.second > it } * (expansion - 1)
            galaxy.first + yMod to galaxy.second + xMod
        }
}

private fun parse(input: List<String>): List<Pair<Int, Int>> = input.mapIndexed { y, row ->
    row.toCharArray().mapIndexed { x, c -> if (c != '.') y to x else null }.filterNotNull()
}.flatten()

private fun createPairs(universe: List<Pair<Int, Int>>): Set<Pair<Pair<Int, Int>, Pair<Int, Int>>> =
    universe.asSequence().map { p1 ->
        universe.map { p2 ->
            p1 to p2
        }
    }.flatten()
        .filter { it.first != it.second }
        .map { when {
            it.first.first < it.second.first -> it
            it.first.first == it.second.first && it.first.second < it.second.second -> it
            else -> it.second to it.first
        } }.toSet()

/* Part 2 */

fun part2(input: List<String>, expansion: Int): Long {
    val galaxies = expand(input, expansion)
    val pairs = createPairs(galaxies)
    return pairs.sumOf { (abs(it.first.first - it.second.first) + abs(it.first.second - it.second.second)).toLong() }
}