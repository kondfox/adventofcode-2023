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
        .associate { it[0] to it[1].split(", ").map { Regex("[A-Z0-9]+").find(it)!!.value} }
    return instructions to map
}

fun visit(from: String, to: String, map: Map<String, List<String>>, instructions: List<Int>): Long {
    var actual = from
    var i = 0L
    while (!actual.endsWith(to))  {
        actual = map[actual]!![instructions[(i++ % instructions.size).toInt()]]
    }
    return i
}

/* Part 2 */

fun part2(input: List<String>): Long {
    val (instructions, map) = parse(input)
    val sources: List<String> = map.keys.filter { it.endsWith("A") }
    val distances = sources.map { visit(it, "Z", map, instructions) }
    return lowestCommonMultiple(distances)
}

fun lowestCommonMultiple(numbers: List<Long>): Long {
    val max = numbers.max()
    var i = 1L
    while (true) {
        val candidate = max * i
        if (numbers.all { candidate % it == 0L }) return candidate
        i++
    }
}