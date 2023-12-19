package dec_17

import java.util.*

fun part1(input: List<String>): Int = solve(parse(input), ::nextNeighbours)

fun part2(input: List<String>): Int = solve(parse(input), ::nextNeighbours2)

private fun parse(input: List<String>): List<List<Field>> = input.mapIndexed {y, row ->
    row.toCharArray().mapIndexed { x, n -> Field(y, x, n.digitToInt()) } }

private fun solve(fields: List<List<Field>>, neighbours: (Path, List<List<Field>>, List<Dir>) -> List<Field>): Int {
    val pq = PriorityQueue<Path>(compareBy { it.score })
    val visited = mutableSetOf<Visit>()
    pq.add(Path(end = fields[0][0]))
    while (pq.peek().end != fields.last().last()) {
        val path = pq.poll()
        if (Visit(path.end, path.history) in visited) continue
        visited.add(Visit(path.end, path.history))
        neighbours(path, fields, path.history).forEach { next -> pq.add(path.clone().add(next)) }
    }
    return pq.poll().score
}

private fun nextNeighbours(path: Path, fields: List<List<Field>>, history: List<Dir>): List<Field> {
    val (y, x) = path.end
    return Dir.entries
        .filter { history.isEmpty() || it != history.last().opposite() }
        .filter { history.isEmpty() || history.size < 3 ||
                history.any { it != history.first() } || it != history.last()
        }
        .mapNotNull { fields.getOrNull(y + it.y)?.getOrNull(x + it.x) }
}

private fun nextNeighbours2(path: Path, fields: List<List<Field>>, history: List<Dir>): List<Field> {
    val (y, x) = path.end
    return Dir.entries
        .filter { history.isEmpty() || it != history.last().opposite() }
        .filter { history.isEmpty() || history.size >= 4 || it == history.last() }
        .filter { history.isEmpty() || history.size < 10 ||
                history.all { it == history.first() } && it != history.last() }
        .mapNotNull { fields.getOrNull(y + it.y)?.getOrNull(x + it.x) }
}

data class Field(val y: Int, val x: Int, val value: Int)

private fun Field.dir(to: Field): Dir = when {
    y > to.y -> Dir.UP
    y < to.y -> Dir.DOWN
    x > to.x -> Dir.LEFT
    x < to.x -> Dir.RIGHT
    else -> throw IllegalArgumentException()
}

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
        history = if (history.isEmpty() || end.dir(field) != history.last()) {
            listOf(end.dir(field))
        } else {
            history + end.dir(field)
        }
        end = field
        return this
    }
}

private fun Path.clone(): Path = Path(score, end, history.toList())

data class Visit(val field: Field, val history: List<Dir>)