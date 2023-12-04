package dec_03

/* Part 1 */

fun part1(input: List<String>): Int = input.joinToString("").let { engine ->
    parseParts(engine, input[0].length, Regex("[^0-9\\.]+")).sumOf { it.num.toInt() }
}

private fun parseParts(engine: String, width: Int, rule: Regex): List<NumPosition> =
        parseNumPositions(engine, width).filter { isPart(it, engine, rule) }

private fun parseNumPositions(engine: String, width: Int): List<NumPosition> = mutableListOf<NumPosition>().also {
    var i = 0
    while (i < engine.length) {
        if (engine[i].isDigit()) {
            parseNumber(engine, i).let { num ->
                it.add(NumPosition(i, num, width))
                i += num.length
            }
        } else {
            i++
        }
    }
}

data class NumPosition(val i: Int, val num: String, val width: Int) {
    private fun left() = if (i % width == 0) i else i - 1
    private fun right() = (i + num.length).let { if ((it % width) == width - 1) it - 1 else it }
    private fun allAbove() = ((left() - width)..(right() - width)).map { it }
    private fun allBelow() = ((left() + width)..(right() + width)).map { it }
    fun perimeter() = allAbove() + listOf(left(), right()) + allBelow()
}

private fun parseNumber(engine: String, i: Int): String =
        engine.substring(i, i + engine.drop(i).indexOfFirst { Regex("[^0-9]").matches(it.toString()) })

private fun isPart(p: NumPosition, engine: String, rule: Regex): Boolean =
        p.perimeter().any { rule.matches(engine.pos(it)) }

private fun String.pos(i: Int): String = if (i in this.indices) this[i].toString() else ""

/* Part 2 */

fun part2(input: List<String>): Int = input.joinToString("").let { engine ->
    parseParts(engine, input[0].length, Regex("[\\*]+"))
            .findGears(engine)
            .sumOf { gear ->
                gear.parts.map { it.num.toInt() }.reduce(Int::times)
            }
}

private fun List<NumPosition>.findGears(engine: String): List<Gear> = this.map { part ->
    connection(engine, part) to part
}.groupBy({ it.first }, { it.second })
    .map { Gear(it.key, it.value) }
    .filter { it.parts.size == 2 }

private fun connection(engine: String, part: NumPosition, symbol: String? = "*"): Int =
    part.perimeter().first { engine.pos(it) == symbol }

data class Gear(val connection: Int, val parts: List<NumPosition>)