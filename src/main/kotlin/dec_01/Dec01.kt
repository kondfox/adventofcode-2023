package dec_01

/* Part 1 */

fun part1(fileLines: List<String>): Int = fileLines.sumOf {
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

fun digit(s: String) = numStrings[s] ?: s

fun part2(fileLines: List<String>): Int {
    val regex = Regex("(?=([0-9]|${numStrings.keys.joinToString("|")}))")

    fun MatchResult.toDigit() = digit(this.groupValues[1])

    return fileLines.sumOf {
            regex.findAll(it).let { matches ->
                "${matches.first().toDigit()}${matches.last().toDigit()}".toInt()
            }
        }
}