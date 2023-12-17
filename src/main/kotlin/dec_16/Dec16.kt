package dec_16

/* Part 1 */

fun part1(input: List<String>): Int {
    val fields = parse(input)
    energize(fields, Direction.RIGHT to fields[0][0])
    return fields.flatten().count { it.energized.any { it.value } }
}

private fun parse(input: List<String>): List<List<Field>> = input.mapIndexed { y, row ->
    row.toCharArray().mapIndexed { x, c -> Field(y, x, fieldType(c)) }
}

private fun energize(fields: List<List<Field>>, start: Pair<Direction, Field>) {
    var toEnergize = mutableListOf(start)
    while (toEnergize.isNotEmpty()) {
        val (dir, field) = toEnergize.first()
        toEnergize = toEnergize.drop(1).toMutableList()
        field.energized[dir] = true
        val to = field.type.beamsTo[dir]!!
        val nexts = to.map { it to fields.next(field, it) }
            .filter { (d, f) -> f != null && !f.energized[d]!! }
            .map { (d, f)  -> d to f!!}
        toEnergize.addAll(nexts)
    }
}

data class Field(val y: Int, val x: Int, val type: FieldType,
                 val energized: MutableMap<Direction, Boolean> = defaultVisitedFrom())

private fun List<List<Field>>.next(from: Field, dir: Direction): Field? =
    this.getOrNull(from.y + dir.y)?.getOrNull(from.x + dir.x)

private fun defaultVisitedFrom(): MutableMap<Direction, Boolean> = mutableMapOf(
    Direction.UP to false,
    Direction.RIGHT to false,
    Direction.DOWN to false,
    Direction.LEFT to false,
)

private fun fieldType(c: Char): FieldType = FieldType.entries.first { it.sign == c }

enum class FieldType(val sign: Char, var beamsTo: Map<Direction, Set<Direction>> = mutableMapOf()) {
    EMPTY('.', beamsTo = mapOf(
        Direction.UP to setOf(Direction.UP),
        Direction.RIGHT to setOf(Direction.RIGHT),
        Direction.DOWN to setOf(Direction.DOWN),
        Direction.LEFT to setOf(Direction.LEFT),
    )),
    LEFT_MIRROR('\\', beamsTo = mapOf(
        Direction.UP to setOf(Direction.LEFT),
        Direction.RIGHT to setOf(Direction.DOWN),
        Direction.DOWN to setOf(Direction.RIGHT),
        Direction.LEFT to setOf(Direction.UP),
    )),
    RIGHT_MIRROR('/', beamsTo = mapOf(
        Direction.UP to setOf(Direction.RIGHT),
        Direction.RIGHT to setOf(Direction.UP),
        Direction.DOWN to setOf(Direction.LEFT),
        Direction.LEFT to setOf(Direction.DOWN),
    )),
    X_SPLITTER('-', mapOf(
        Direction.UP to setOf(Direction.LEFT, Direction. RIGHT),
        Direction.DOWN to setOf(Direction.LEFT, Direction. RIGHT),
        Direction.RIGHT to setOf(Direction.RIGHT),
        Direction.LEFT to setOf(Direction.LEFT),
    )),
    Y_SPLITTER('|', mapOf(
        Direction.RIGHT to setOf(Direction.UP, Direction.DOWN),
        Direction.LEFT to setOf(Direction.UP, Direction.DOWN),
        Direction.UP to setOf(Direction.UP),
        Direction.DOWN to setOf(Direction.DOWN),
    ))

}

enum class Direction(val y: Int, val x: Int) {
    UP(-1, 0), RIGHT(0, 1), DOWN(1, 0), LEFT(0, -1)
}