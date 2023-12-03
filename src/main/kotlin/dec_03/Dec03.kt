package dec_03

/* Part 1 */

fun part1(input: List<String>): Int {
    return input.joinToString("").let { engine ->
        collectNumbersWithIndices(engine).filter {
            isPart(it, engine, input[0].length, Regex("[^0-9\\.]+"))
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

private fun isPart(indexWithNum: Map.Entry<Int, String>, engine: String, width: Int, regex: Regex): Boolean {
    val (i, num) = indexWithNum
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

/* Part 2 */

fun part2(input: List<String>): Int {
    return input.joinToString("").let { engine ->
        val parts = collectNumbersWithIndices(engine).filter {
            isPart(it, engine, input[0].length, Regex("[\\*]+"))
        }
        val gearConnections = parts.map { connection(engine, it, input[0].length) }
                .groupingBy { it }.eachCount()
                .filter { it.value == 2 }
                .keys
        val gears = gearConnections.associate {
            it to parts.filter { part -> connection(engine, part, input[0].length) == it }
        }
        return gears.values.sumOf { gear ->
            gear.map { it.value.toInt() }.reduce(Int::times)
        }
    }
}

private fun connection(engine: String, part: Map.Entry<Int, String>, width: Int, symbol: String? = "*"): Int {
    val (i, num) = part
    return when {
        engine.pos(leftOf(i)) == symbol -> i - 1
        engine.pos(rightOf(i + num.length - 1)) == symbol -> i + num.length
        (-1..num.length).any { engine.pos(aboveOf(i + it, width)) == symbol } -> {
            return (-1..num.length).first { engine.pos(aboveOf(i + it, width)) == symbol }.let {
                aboveOf(i + it, width)
            }
        }
        else -> {
            return (-1..num.length).first { engine.pos(belowOf(i + it, width)) == symbol }.let {
                belowOf(i + it, width)
            }
        }
    }
}