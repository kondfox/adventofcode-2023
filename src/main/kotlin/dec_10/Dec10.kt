package dec_10

/* Part 1 */

fun part1(input: List<String>): Int {
    val nodes = parse(input)
    val start = nodes.firstNotNullOf { it.firstOrNull { it.pipe == 'S' } }
    val neighbours: List<Node> = neighbours(start, nodes)
    val loop = findLoop(nodes, neighbours.first())
    return Math.ceil(loop.size / 2.0).toInt()
}

private fun parse(input: List<String>): List<List<Node>> = input.mapIndexed { y, line ->
    line.toCharArray().mapIndexed { x, pipe -> Node(x, y, pipe) }
}

data class Node(val x: Int, val y: Int, val pipe: Char) {
    private fun directions(): List<Pair<Int, Int>> = when (pipe) {
        '|' -> listOf(1 to 0, -1 to 0)
        '-' -> listOf(0 to 1, 0 to -1)
        'L' -> listOf(-1 to 0, 0 to 1)
        'J' -> listOf(-1 to 0, 0 to -1)
        '7' -> listOf(1 to 0, 0 to -1)
        'F' -> listOf(1 to 0, 0 to 1)
        'S' -> listOf(-1 to 0, 0 to 1, 1 to 0, 0 to -1)
        else -> emptyList()
    }
    fun connections(): List<Pair<Int, Int>> = directions().map { (dy, dx) -> y + dy to x + dx }
    fun reachableFrom(other: Node): Boolean = this.connections().contains(other.y to other.x)
}

private fun neighbours(node: Node, nodes: List<List<Node>>): List<Node> {
    val connections = node.connections()
    val notNulls = connections.filter { (y, x) -> nodes.getOrNull(y)?.getOrNull(x) != null }
    val neighbours = notNulls.map { (y, x) -> nodes[y][x] }
    val valids = neighbours.filter { it.reachableFrom(node) && it.pipe != 'S' }
    return valids
}

private fun findLoop(nodes: List<List<Node>>, start: Node): List<Node> {
    val visited = mutableListOf(start)
    var actual = start
    while (true) {
        val neighbours = neighbours(actual, nodes)
        val next = neighbours.firstOrNull { !visited.contains(it) } ?: return visited
        visited.add(next)
        actual = next
    }
}