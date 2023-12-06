package dec_06

fun part1() = solve(listOf(7L, 15L, 30L).zip(listOf(9L, 40L, 200L)))

fun part2() = solve(listOf(71530L to 940200L))

private fun solve(races: List<Pair<Long, Long>>) = races
    .map { (time, distance) -> quadratic(1, -time, distance) }
    .map { it.second - it.first - 1 }
    .reduce(Long::times)

private fun quadratic(a: Int, b: Long, c: Long): Pair<Long, Long> {
    val d = Math.sqrt(b.toDouble() * b - 4 * a * c)
    return Math.floor((-b - d) / (2 * a)).toLong() to Math.ceil((-b + d) / (2 * a)).toLong()
}