package dec_04

import kotlin.math.pow

/* Part 1 */

fun part1(input: List<String>): Int = input.sumOf { score(parse(it).countWinners()) }

private fun parse(card: String): Card = card
    .split(": ")[1]
    .split(" | ")
    .map { it.trim().split(Regex("[ ]+")).toSet() }.let {
        Card(it[0], it[1])
    }

data class Card(val winnerNumbers: Set<String>, val myNumbers: Set<String>) {
    fun countWinners(): Int = myNumbers.count { it in winnerNumbers }
}

private fun score(count: Int) =  2.0.pow(count - 1).toInt()

/* Part 2 */

fun part2(input: List<String>): Int = input.indices.fold(mutableMapOf<Int, Int>()) { cardCounts, i ->
    addCopyCount(cardCounts, i, parse(input[i]).countWinners(), input.size)
}.let { cardCounts ->
    cardCounts.values.sum() + input.size - cardCounts.size
}

private fun addCopyCount(cardCounts: MutableMap<Int, Int>, i: Int, count: Int, limit: Int): MutableMap<Int, Int> {
    ((i + 1)..(i + count)).forEach { copyIndex ->
        if (copyIndex < limit) {
            val actual = cardCounts[i] ?: 1
            cardCounts[copyIndex] = cardCounts[copyIndex]?.plus(actual) ?: (actual + 1)
        }
    }.let { return cardCounts }
}