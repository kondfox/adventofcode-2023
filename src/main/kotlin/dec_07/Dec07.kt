package dec_07

import kotlin.math.pow

fun part1(input: List<String>): Long = solve(
    input.map(::parse),
    GameRule(listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A'), false)
)

fun part2(input: List<String>): Long = solve(
    input.map(::parse),
    GameRule(listOf('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A'), true)
)

private fun parse(input: String): Hand = input.split(" ").let { Hand(it[0], it[1].toLong()) }

class Hand(val hand: String, val bid: Long, var value: Int? = 0, var highCard: Int? = 0)

data class GameRule(val cardOrder: List<Char>, val withJoker: Boolean) {
    fun evaluateHand(hand: Hand): Hand = hand.apply {
        this.value = handValue(hand.hand, withJoker)
        this.highCard = highCardValue(hand.hand, cardOrder)
    }
}

fun solve(hands: List<Hand>, rule: GameRule): Long = hands
    .map { rule.evaluateHand(it) }
    .sortedWith(compareBy({ it.value }, { it.highCard }))
    .mapIndexed { i, hand -> hand.bid * (i + 1) }
    .sum()

private fun handValue(hand: String, withJoker: Boolean): Int = hand.groupingBy { it }.eachCount().let {
    return when {
        it.nOfAKind(5, withJoker) -> 7
        it.nOfAKind(4, withJoker) -> 6
        it.nAndMOfAKind(3, 2, withJoker) -> 5
        it.nOfAKind(3, withJoker) -> 4
        it.nAndMOfAKind(2, 2, withJoker) -> 3
        it.nOfAKind(2, withJoker) -> 2
        else -> 1
    }
}

private fun highCardValue(hand: String, cards: List<Char>): Int {
    val cardValues = cards.withIndex().associate { it.value to it.index + 1 }
    return hand.indices.sumOf { i -> (cardValues[hand[i]]!! * 13.0.pow(hand.length - i - 1)).toInt() }
}

private fun Map<Char, Int>.nOfAKind(n: Int, withJoker: Boolean) =
    this.mostFrequentValue() + (if (withJoker && this.contains('J')) this['J']!! else 0) == n
private fun Map<Char, Int>.nOfAKind(n: Int, used: Int, withJoker: Boolean) = this.nOfAKind(n + used, withJoker)
private fun Map<Char, Int>.nAndMOfAKind(n: Int, m: Int, withJoker: Boolean) = this.nOfAKind(n, withJoker) &&
    this.without(this.mostFrequent()!!.key).nOfAKind(m, n - this.mostFrequentValue(), withJoker)

private fun Map<Char, Int>.without(c: Char): Map<Char, Int> = this.filter { it.key != c }
private fun Map<Char, Int>.mostFrequentValue(): Int = this.mostFrequent()?.value ?: 0
private fun Map<Char, Int>.mostFrequent(): Map.Entry<Char, Int>? = this.without('J').let {
    if (it.isNotEmpty()) it.maxBy { it.value } else null
}