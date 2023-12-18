package dec_17

import java.util.*

/* Part 1 */

fun part1(input: List<String>): Int {
    val fields = parse(input)
    val pq = PriorityQueue<Path>(compareBy { it.score })
    val visited = mutableSetOf<Visit>()
    pq.add(Path(end = fields[0][0]))
    while (pq.peek().end != fields.last().last()) {
        val path = pq.poll()
        if (Visit(path.end, path.history) in visited) continue
        visited.add(Visit(path.end, path.history))
        path.nextNeighbours(fields, path.history).forEach { next -> pq.add(path.clone().add(next)) }
    }
    return pq.poll().score
}

private fun parse(input: List<String>): List<List<Field>> = input.mapIndexed {y, row ->
    row.toCharArray().mapIndexed { x, n -> Field(y, x, n.digitToInt()) } }

data class Field(val y: Int, val x: Int, val value: Int)

data class Visit(val field: Field, val history: List<Dir>)

enum class Dir(val y: Int, val x: Int) {
    UP(-1, 0), RIGHT(0, 1), DOWN(1, 0), LEFT(0, -1)
}

private fun Dir.opposite(): Dir = when (this) {
    Dir.UP -> Dir.DOWN
    Dir.DOWN -> Dir.UP
    Dir.LEFT -> Dir.RIGHT
    Dir.RIGHT -> Dir.LEFT
}

data class Path(var score: Int = 0, var end: Field, var history: List<Dir> = listOf()) {
    fun add(field: Field): Path {
        score += field.value
        history = (history + end.dir(field)).takeLast(3)
        end = field
        return this
    }
}

private fun Path.clone(): Path = Path(score, end, history.toList())

private fun Path.nextNeighbours(fields: List<List<Field>>, history: List<Dir>): List<Field> {
    val (y, x) = end
    return Dir.entries
        .filter { history.isEmpty() || it != history.last().opposite() }
        .filter { history.isEmpty() || history.size < 3 || history.any {
            it != history.first() } || it != history.last()
        }
        .mapNotNull { fields.getOrNull(y + it.y)?.getOrNull(x + it.x) }
}

private fun Field.dir(to: Field): Dir = when {
    y > to.y -> Dir.UP
    y < to.y -> Dir.DOWN
    x > to.x -> Dir.LEFT
    x < to.x -> Dir.RIGHT
    else -> throw IllegalArgumentException()
}