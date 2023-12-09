package dec_09

/* Part 1 */

fun part1(input: List<String>): Int = input.sumOf {
    it.split(" ").map(String::toInt).next()
}

private fun List<Int>.next(): Int {
    var series = this.toMutableList()
    val next = mutableListOf(series.last())
    while (series.any { it != 0}) {
        series = series.indices.fold(mutableListOf()) { diffs, i ->
            if (i > 0) {
                diffs.add(series[i] - series[i - 1])
            }
            diffs
        }
        next.add(series.last())
    }
    return next.sum()
}

/* Part 2 */

fun part2(input: List<String>): Int = input.sumOf {
    it.split(" ").map(String::toInt).previous()
}

private fun List<Int>.previous(): Int {
    var series = this.toMutableList()
    val previous = mutableListOf(series.first())
    while (series.any { it != 0}) {
        series = series.indices.fold(mutableListOf()) { diffs, i ->
            if (i > 0) {
                diffs.add(series[i] - series[i - 1])
            }
            diffs
        }
        previous.add(series.first())
    }
    return previous.indices.reversed().fold(0) { prev, i ->
        if (i < previous.lastIndex) previous[i] - prev else prev
    }
}