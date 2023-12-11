package dec_11

import kotlin.math.abs

/* Part 1 */

fun part1(input: List<String>): Int {
    val universe = parse(expand(input))
    val pairs = createPairs(universe)
    return pairs.sumOf { abs(it.first.first - it.second.first) + abs(it.first.second - it.second.second) }
}

private fun expand(universe: List<String>): List<String> {
    val expanded = mutableListOf<String>()
    universe.forEach { row ->
        if (row.toCharArray().all { it == '.' }) {
            expanded.addAll(listOf(row, row))
        } else {
            expanded.add(row)
        }
    }
    val expandedCols = universe[0].indices.filter { x -> universe.map { it[x] }.all { it == '.' } }
    return expanded.map { row ->
        row.mapIndexed { x, c -> if (x in expandedCols) ".." else c }.joinToString("")
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