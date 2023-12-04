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