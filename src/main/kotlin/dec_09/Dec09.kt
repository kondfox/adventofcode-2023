package dec_09

fun part1(input: List<String>): Int = solve(input)

fun part2(input: List<String>): Int = solve(input, true)

fun solve(input: List<String>, isBackward: Boolean = false): Int = input.sumOf {
    it.split(" ").map(String::toInt).next(isBackward)
}

private fun List<Int>.next(isBackward: Boolean = false, series: MutableList<Int> = mutableListOf()): Int = when {
    this.all { it == 0 } -> series.reversed().reduce { acc, n -> n + if (isBackward) -acc else acc }
    else -> this.indices.drop(1).fold(mutableListOf<Int>()) { diffs, i ->
                diffs.also { it.add(this[i] - this[i - 1]) }
            }.next(isBackward, series.also { it.add(if (isBackward) this.first() else this.last()) })
}