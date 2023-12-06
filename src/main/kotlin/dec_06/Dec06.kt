package dec_06

/* Part 1 */

fun part1(input: List<String>) = solve(parse(input))

private fun parse(input: List<String>): List<Pair<Long, Long>> {
    val (time, distance) = input.windowed(2)
        .flatMap { it.map { it.split(":")[1].trim() } }
        .map { Regex("[0-9]+").findAll(it).map { it.value.toLong() }.toList() }.toList()
    return time.zip(distance)
}

private fun solve(races: List<Pair<Long, Long>>) = races
    .map { (time, distance) -> quadratic(1, -time, distance).correctEquals() }
    .map { Math.ceil(it.second).toLong() - Math.ceil(it.first).toLong() }
    .reduce(Long::times)

private fun quadratic(a: Int, b: Long, c: Long): Pair<Double, Double> {
    val d = Math.sqrt(b.toDouble() * b - 4 * a * c)
    return ((-b - d) / (2 * a)) to ((-b + d) / (2 * a))
}

private fun Pair<Double, Double>.correctEquals() = when {
    (first * 10) % 10 == 0.0 -> first + 1 to second - 0.99
    else -> this
}

/* Part 2 */

fun part2(input: List<String>) = solve(parse2(input))

private fun parse2(input: List<String>): List<Pair<Long, Long>> {
    return input.windowed(2)
        .flatMap { it.map { it.split(":")[1].trim() } }
        .map { Regex("[0-9]+").findAll(it).fold("") { num, n -> num + n.value }.toLong() }
        .zipWithNext()
}