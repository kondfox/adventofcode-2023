package dec_06

/* Part 1 */

fun part1(input: List<String>): Int {
    return parse(input)
        .map { (time, distance) -> quadratic(1, -time, distance).correctEquals() }
        .map { Math.ceil(it.second).toInt() - Math.ceil(it.first).toInt() }
        .reduce(Int::times)
}

private fun parse(input: List<String>): List<Pair<Int, Int>> {
    val (time, distance) = input.windowed(2)
        .flatMap { it.map { it.split(":")[1].trim() } }
        .map { Regex("[0-9]+").findAll(it).map { it.value.toInt() }.toList() }.toList()
    return time.zip(distance)
}

private fun quadratic(a: Int, b: Int, c: Int): Pair<Double, Double> {
    val d = Math.sqrt(b.toDouble() * b - 4 * a * c)
    return ((-b - d) / (2 * a)) to ((-b + d) / (2 * a))
}

private fun Pair<Double, Double>.correctEquals() = when {
    (first * 10) % 10 == 0.0 -> first + 1 to second - 0.99
    else -> this
}
