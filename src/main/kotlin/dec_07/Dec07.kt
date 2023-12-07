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

/* Part 2 */

fun part2(input: List<String>): Long {
    return input.map(::parse2)
        .sortedWith(compareBy({ it.value }, { it.highCard }))
        .mapIndexed { i, hand -> hand.bid * (i + 1) }
        .sum()
}

data class Hand2(val hand: String, val bid: Long, val value: Int = evaluate2(hand), val highCard: Int = highCard2(hand))

private fun parse2(input: String): Hand2 = input.split(" ").let { Hand2(it[0], it[1].toLong()) }

private fun evaluate2(hand: String): Int = hand.groupingBy { it }.eachCount().let {
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

val CARDS2: Map<Char, Int> = listOf('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A').withIndex()
    .associate { it.value to it.index + 1 }

private fun highCard2(hand: String): Int =
    hand.indices.sumOf { i -> (CARDS2[hand[i]]!! * 13.0.pow(hand.length - i - 1)).toInt() }

private fun Map<Char, Int>.hasNOfAKind(n: Int) = this.without('J').values.contains(n)
private fun Map<Char, Int>.hasNOfAKindWithJokers(n: Int): Boolean {
    val yes = this.mostFrequentValue() + this.jokerCount() == n
    return yes
}
private fun Map<Char, Int>.hasNOfAKindWithJokers(n: Int, used: Int) = this.mostFrequentValue() + this.jokerCount() - used == n
private fun Map<Char, Int>.jokerCount() = this['J'] ?: 0
private fun Map<Char, Int>.jokersUsedForNOfAKind(n: Int) = n - this.mostFrequentValue()
private fun Map<Char, Int>.mostFrequent(): Map.Entry<Char, Int>? = this.without('J').let {
    if (it.isNotEmpty()) it.maxBy { it.value } else null
}
private fun Map<Char, Int>.mostFrequentValue(): Int = this.mostFrequent()?.value ?: 0
private fun Map<Char, Int>.mostFrequentKey(): Char = this.mostFrequent()!!.key
private fun Map<Char, Int>.without(c: Char): Map<Char, Int> = this.filter { it.key != c }
private fun Map<Char, Int>.hasNAndMOfAKind(n: Int, m: Int): Boolean = when {
    this.hasNOfAKind(n) && this.without(this.mostFrequentKey()).hasNOfAKind(m) -> true
    this.hasNOfAKind(n) && this.without(this.mostFrequentKey()).hasNOfAKindWithJokers(m) -> true
    this.hasNOfAKindWithJokers(n) && this.without(this.mostFrequentKey()).hasNOfAKind(m) -> true
    this.hasNOfAKindWithJokers(n) && this.without(this.mostFrequentKey()).hasNOfAKindWithJokers(m, this.jokersUsedForNOfAKind(n)) -> true
    else -> false
}