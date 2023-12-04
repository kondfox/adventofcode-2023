package dec_04

import kotlin.math.pow

/* Part 1 */

fun part1(input: List<String>): Int {
    return input.sumOf { card ->
        val (winnerNumbers, myNumbers) = card.split(": ")[1]
            .split(" | ")
            .map { it.trim().split(Regex("[ ]+")).toSet() }
         myNumbers.count { it in winnerNumbers }.let {
             2.0.pow(it - 1).toInt()
         }
    }
}

/* Part 2 */

fun part2(input: List<String>): Int {
    return input.indices.fold(mutableMapOf<Int, Int>()) { cardCounts, i ->
        val (winnerNumbers, myNumbers) = input[i].split(": ")[1]
            .split(" | ")
            .map { it.trim().split(Regex("[ ]+")).toSet() }
        myNumbers.count { it in winnerNumbers }.let {
            ((i + 1)..(i + it)).forEach { cardIndex ->
                if (cardIndex in input.indices) {
                    val actual = cardCounts[i] ?: 1
                    cardCounts[cardIndex] = cardCounts[cardIndex]?.plus(actual) ?: (actual + 1)
                }
            }
        }
        cardCounts
    }.let { cardCounts ->
        cardCounts.values.sum() + input.size - cardCounts.size
    }
}