package dec_03

/* Part 1 */

fun part1(input: List<String>): Int {
    return input.joinToString("").let { engine ->
        collectNumbersWithIndices(engine).filter {
            isPart(it, engine, input[0].length)
        }.values.sumOf { it.toInt() }
    }
}

private fun collectNumbersWithIndices(engine: String): Map<Int, String> {
    val numbers = mutableMapOf<Int, String>()
    var num = ""
    var numIdx = 0
    engine.indices.forEach { i ->
        if (engine[i].isDigit()) {
            if (num.isEmpty()) {
                numIdx = i
            }
            num += engine[i]
        } else if (num.isNotEmpty()) {
            numbers[numIdx] = num
            num = ""
            numIdx = i
        }
    }
    return numbers
}

private fun isPart(indexWithNum: Map.Entry<Int, String>, engine: String, width: Int): Boolean {
    val (i, num) = indexWithNum
    val regex = Regex("[^0-9\\.]+")
    return when {
        regex.matches(engine.pos(leftOf(i))) -> true
        regex.matches(engine.pos(rightOf(i + num.length - 1))) -> true
        (-1..num.length).any() { regex.matches(engine.pos(aboveOf(i + it, width))) } -> true
        (-1..num.length).any() { regex.matches(engine.pos(belowOf(i + it, width))) } -> true
        else -> false
    }
}

private fun leftOf(i: Int) = i - 1
private fun rightOf(i: Int) = i + 1
private fun aboveOf(i: Int, width: Int) = i - width
private fun belowOf(i: Int, width: Int) = i + width

private fun String.pos(i: Int): String = if (i in this.indices) this[i].toString() else ""
