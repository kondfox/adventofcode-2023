package dec_15

/* Part 1 */

fun part1(input: List<String>): Int = input[0].split(",").sumOf(::hash)

private fun hash(s: String): Int =
    s.toCharArray().map(Char::code).fold(0) { hash, c -> ((hash + c) * 17) % 256}