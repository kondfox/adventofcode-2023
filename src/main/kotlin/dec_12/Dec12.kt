package dec_12

import kotlin.math.min

/* Part 1 */

fun part1(input: List<String>): Int = input.sumOf {
    findVariations(required(it), it.split(" ")[0]).size
}

fun required(line: String): String {
    return line.split(" ")[1].split(",").map { it.toInt() }
        .map { List(it) { '#' }.joinToString("") }
        .joinToString(" ")
}

fun findVariations(required: String, to: String): List<String> {
    var variations: List<String> = mutableListOf("")
    val toTrimmed = to.toCharArray().map { if (it == '.') ' ' else it}.joinToString("").trim()
    toTrimmed.forEach { c ->
        if (c != '?') {
            variations = variations.map { it + c }
        } else {
            variations = variations.map { it + ' ' } + variations.map { it + '#' }
        }
        variations = variations.filter { same(required, it) }
    }
    variations = variations.filter { it.superTrim().length == required.length }
    return variations
}

fun same(expected: String, actual: String): Boolean {
    return actual.superTrim() == expected.substring(0, min(actual.superTrim().length, expected.length))
}

fun String.superTrim(): String = this.trim().replace(Regex("[ ]+"), " ")