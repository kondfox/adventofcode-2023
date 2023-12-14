package dec_13

/* Part 1 */

fun part1(input: List<String>): Int = parse(input).sumOf(::findMirror)

private fun parse(input: List<String>): List<Note> {
    return input.fold(mutableListOf(mutableListOf<String>())) { notes, line ->
        if (line.isEmpty()) {
            notes.add(mutableListOf())
        } else {
            notes.last().add(line)
        }
        notes
    }.map { Note(it, it.rotateRight()) }
}

data class Note(val normal: List<String>, val rotated: List<String>)

private fun List<String>.rotateRight(): List<String> {
    val rotated: MutableList<String> = MutableList(this[0].length) { "" }
    for (col in this[0].indices) {
        for (row in this.indices.reversed()) {
            rotated[col] += this[row][col].toString()
        }
    }
    return rotated
}

private fun findMirror(note: Note): Int {
    val top = findMirror(note.normal)
    if (top != null) return top * 100
    return findMirror(note.rotated)!!
}

private fun findMirror(note: List<String>): Int? {
    var actual = note
    var dropped = 0
    val minSize = 2
    while (!actual.isMirrored() && actual.size >= minSize) {
        actual = actual.drop(1)
        dropped++
    }
    if (actual.size >= minSize) return dropped + actual.size / 2
    actual = note
    dropped = 0
    while (!actual.isMirrored() && actual.size >= minSize) {
        actual = actual.dropLast(1)
        dropped++
    }
    if (actual.size >= minSize) return actual.size / 2
    return null
}

private fun List<String>.isMirrored(): Boolean = this.size % 2 == 0 &&
    (0..this.lastIndex / 2).all { this[it] == this[this.lastIndex - it] }