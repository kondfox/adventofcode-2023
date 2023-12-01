package dec_01

/* Part 1 */

fun part1(input: List<String>): Int = input.sumOf {
     "${it.first(Character::isDigit)}${it.last(Character::isDigit)}".toInt()
}

/* Part 2 */

val numStrings = mapOf(
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9",
)

fun part2(input: List<String>): Int = input.sumOf { row ->
    Regex("(?=([0-9]|${numStrings.keys.joinToString("|")}))")
        .findAll(row).let { matches ->
            "${matches.first().toDigit()}${matches.last().toDigit()}".toInt()
        }
}

private fun MatchResult.toDigit() = digit(this.groupValues[1])

private fun digit(s: String) = numStrings[s] ?: s
