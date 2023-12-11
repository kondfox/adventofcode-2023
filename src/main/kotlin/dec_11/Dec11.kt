package dec_11

import kotlin.math.abs

fun part1(input: List<String>): Long = solve(input, parse(input))

fun part2(input: List<String>, expansion: Int): Long = solve(input, parse(input), expansion)

fun solve(universe: List<String>, galaxies: List<Pair<Int, Int>>, expansion: Int = 2): Long {
    return expand(universe, galaxies, expansion).let(::createPairs).sumOf {
        (abs(it.first.first - it.second.first) + abs(it.first.second - it.second.second)).toLong()
    }
}

private fun expand(universe: List<String>, galaxies: List<Pair<Int, Int>>, rate: Int = 2): List<Pair<Int, Int>> {
    val expandedRows = universe.indices.filter { y -> universe[y].all { it == '.' } }
    val expandedCols = universe[0].indices.filter { x -> universe.map { it[x] }.all { it == '.' } }
    return galaxies.map { galaxy ->
        galaxy.first + expandedRows.count { galaxy.first > it } * (rate - 1) to
                galaxy.second + expandedCols.count { galaxy.second > it } * (rate - 1) }
}

private fun parse(input: List<String>): List<Pair<Int, Int>> = input.mapIndexed { y, row ->
    row.toCharArray().mapIndexed { x, c -> if (c != '.') y to x else null }.filterNotNull()
}.flatten()

private fun createPairs(galaxies: List<Pair<Int, Int>>): Set<Pair<Pair<Int, Int>, Pair<Int, Int>>> =
    galaxies.asSequence().map { g1 -> galaxies.map { g2 -> g1 to g2 } }.flatten()
        .filter { it.first != it.second }
        .map { when {
            it.first.first < it.second.first -> it
            it.first.first == it.second.first && it.first.second < it.second.second -> it
            else -> it.second to it.first
        } }.toSet()