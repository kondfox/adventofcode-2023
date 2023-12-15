package dec_15

import java.util.Deque

/* Part 1 */

fun part1(input: String): Int = input.split(",").sumOf(::hash)

private fun hash(s: String): Int =
    s.toCharArray().map(Char::code).fold(0) { hash, c -> ((hash + c) * 17) % 256}

/* Part 2 */
fun part2(input: String): Int {
    val boxes = MutableList(256) { ArrayDeque<String>() }
    input.split(",").forEach { instruction ->
        val op = instruction.first { it == '=' || it == '-' }
        val (label, focal) = instruction.split(op)
        val box = boxes[hash(label)]
        val existing = box.indexOfFirst { it.split(" ")[0] == label }
        when (op) {
            '=' -> {
                if (existing == -1) {
                    box.addLast("$label $focal")
                } else {
                    box[existing] = "$label $focal"
                }
            }
            '-' -> if (existing > -1) box.removeAt(existing)
        }
    }
    return boxes.mapIndexed { i, box ->
        box.mapIndexed { j, lens ->
            val res = (i + 1) * (j + 1) * lens.split(" ")[1].toInt()
            res
        }.sum()
    }.sum()
}