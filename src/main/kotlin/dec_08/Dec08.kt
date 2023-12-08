package dec_08

/* Part 1 */

fun part1(input: List<String>): Long {
    val (instructions, map) = parse(input)
    return visit("AAA", "ZZZ", map, instructions)
}

fun parse(input: List<String>): Pair<List<Int>, Map<String, List<String>>> {
    val instructions = input[0].toCharArray().map { if (it == 'L') 0 else 1 }.toList()
    val map = input.drop(2)
        .map { it.split(" = ")}
        .associate { it[0] to it[1].split(", ").map { Regex("[A-Z]+").find(it)!!.value} }
    return instructions to map
}

fun visit(from: String, to: String, map: Map<String, List<String>>, instructions: List<Int>): Long {
    var now = from
    var i = 0L
    while (now != to)  {
        now = map[now]!![instructions[(i++ % instructions.size).toInt()]]
    }
    return i
}