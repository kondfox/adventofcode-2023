package dec_09

fun part1(input: List<String>): Int = solve(input)

fun part2(input: List<String>): Int = solve(input, true)

fun solve(input: List<String>, isBackward: Boolean = false): Int = input.sumOf {
    it.split(" ").map(String::toInt).next(isBackward)
}

private fun List<Int>.next(isBackward: Boolean = false): Int {
    var series = this.toMutableList()
    val next = mutableListOf(if (isBackward) series.first() else series.last())
    while (series.any { it != 0}) {
        series = series.indices.drop(1).fold(mutableListOf()) { diffs, i ->
            diffs.also { it.add(series[i] - series[i - 1]) }
        }
        next.add(if (isBackward) series.first() else series.last())
    }
    return next.indices.reversed().drop(1).fold(0) { prev, i ->
        next[i] + if (isBackward) -prev else prev
    }
}