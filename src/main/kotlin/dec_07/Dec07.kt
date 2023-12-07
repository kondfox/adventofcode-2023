package dec_07

import kotlin.math.pow

/* Part 1 */

fun part1(input: List<String>): Long = solve(
    input.map(::parse),
    GameRule(::valueWithoutJoker, listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A'))
)

private fun parse(input: String): Hand = input.split(" ").let { Hand(it[0], it[1].toLong()) }

class Hand(val hand: String, val bid: Long, var value: Int? = 0, var highCard: Int? = 0)

data class GameRule(val calculateValue: (String) -> Int, val cardOrder: List<Char>) {
    fun evaluateHand(hand: Hand): Hand = hand.apply {
        this.value = calculateValue(hand.hand)
        this.highCard = calculateHighCard(hand.hand, cardOrder)
    }
}

fun solve(hands: List<Hand>, rule: GameRule): Long = hands
    .map { rule.evaluateHand(it) }
    .sortedWith(compareBy({ it.value }, { it.highCard }))
    .mapIndexed { i, hand -> hand.bid * (i + 1) }
    .sum()

private fun calculateHighCard(hand: String, cards: List<Char>): Int {
    val cardValues = cards.withIndex().associate { it.value to it.index + 1 }
    return hand.indices.sumOf { i -> (cardValues[hand[i]]!! * 13.0.pow(hand.length - i - 1)).toInt() }
}

private fun valueWithoutJoker(hand: String): Int = hand.groupingBy { it }.eachCount().let {
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

/* Part 2 */

fun part2(input: List<String>): Long = solve(
    input.map(::parse),
    GameRule(::valueWithJoker, listOf('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A'))
)

private fun valueWithJoker(hand: String): Int = hand.groupingBy { it }.eachCount().let {
    return when {
        it.hasNOfAKindWithJokers(5) -> 7
        it.hasNOfAKindWithJokers(4) -> 6
        it.hasNAndMOfAKind(3, 2) -> 5
        it.hasNOfAKindWithJokers(3) -> 4
        it.hasNAndMOfAKind(2, 2) -> 3
        it.hasNOfAKindWithJokers(2) -> 2
        else -> 1
    }
}

private fun Map<Char, Int>.hasNOfAKindWithJokers(n: Int) = this.mostFrequentValue() + (this['J'] ?: 0) == n
private fun Map<Char, Int>.hasNOfAKindWithJokers(n: Int, used: Int) = this.hasNOfAKindWithJokers(n + used)
private fun Map<Char, Int>.jokersUsedForNOfAKind(n: Int) = n - this.mostFrequentValue()
private fun Map<Char, Int>.mostFrequent(): Map.Entry<Char, Int>? = this.without('J').let {
    if (it.isNotEmpty()) it.maxBy { it.value } else null
}
private fun Map<Char, Int>.mostFrequentValue(): Int = this.mostFrequent()?.value ?: 0
private fun Map<Char, Int>.mostFrequentKey(): Char = this.mostFrequent()!!.key
private fun Map<Char, Int>.without(c: Char): Map<Char, Int> = this.filter { it.key != c }
private fun Map<Char, Int>.hasNAndMOfAKind(n: Int, m: Int): Boolean = this.hasNOfAKindWithJokers(n) &&
        this.without(this.mostFrequentKey()).hasNOfAKindWithJokers(m, this.jokersUsedForNOfAKind(n))