package dec_07

import kotlin.math.pow

/* Part 1 */

fun part1(input: List<String>): Long {
    return input.map(::parse)
        .sortedWith(compareBy({ it.value }, { it.highCard }))
        .mapIndexed { i, hand -> hand.bid * (i + 1) }
        .sum()
}

val CARDS: Map<Char, Int> = listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A').withIndex()
    .associate { it.value to it.index + 1 }

data class Hand(val hand: String, val bid: Long, val value: Int = evaluate(hand), val highCard: Int = highCard(hand))

private fun parse(input: String): Hand = input.split(" ").let { Hand(it[0], it[1].toLong()) }

private fun evaluate(hand: String): Int = hand.groupingBy { it }.eachCount().let {
    return when {
        it.keys.size == 1 -> 7
        it.values.contains(4) -> 6
        it.values.contains(3) && it.values.contains(2) -> 5
        it.values.contains(3) -> 4
        it.values.count { it == 2 } == 2 -> 3
        it.values.contains(2) -> 2
        else -> 1
    }
}

private fun highCard(hand: String): Int =
    hand.indices.sumOf { i -> (CARDS[hand[i]]!! * 13.0.pow(hand.length - i - 1)).toInt() }