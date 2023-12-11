package dec_10

/* Part 1 */

fun part1(input: List<String>): Int = Math.ceil(findLoop(parse(input)).size / 2.0).toInt()

private fun parse(input: List<String>): List<List<Node>> = input.mapIndexed { y, line ->
    line.toCharArray().mapIndexed { x, pipe -> Node(x, y, pipe) }
}

private fun findLoop(nodes: List<List<Node>>): List<Node> {
    val start = nodes.firstNotNullOf { it.firstOrNull { it.pipe == 'S' } }
    val first = neighbours(start, nodes).first()
    val visited = mutableListOf(start, first)
    var actual = first
    while (true) {
        val neighbours = neighbours(actual, nodes)
        val next = neighbours.firstOrNull { !visited.contains(it) } ?: return visited
        visited.add(next)
        actual = next
    }
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

private fun neighbours(node: Node, nodes: List<List<Node>>): List<Node> = node.connections()
    .filter { (y, x) -> nodes.getOrNull(y)?.getOrNull(x) != null }
    .map { (y, x) -> nodes[y][x] }
    .filter { it.reachableFrom(node) && it.pipe != 'S' }

/* Part 2 */

fun part2(input: List<String>): Int {
    val nodes = parse(input)
    val loop = findLoop(nodes)
    val doubleNodes = doubleNodes(nodes)
    val doubleLoop = findLoop(doubleNodes)
    val firstInLoop = findFirstInLoop(doubleLoop)
    val areaTiles: Set<Node> = firstInLoop
        .map { findAreaTiles(doubleNodes, doubleLoop, it) }
        .first { !it.any { node -> node.x == 0 || node.y == 0 || node.x == doubleNodes[0].lastIndex || node.y == doubleNodes.lastIndex } }
    val realAreaTiles = areaTiles
        .filter { it.x % 2 == 0 && it.y % 2 == 0 }
        .map { nodes[it.y / 2][it.x / 2] }
        .filter { !loop.contains(it) }
    print(nodes, loop, realAreaTiles) // because it's beautiful <3
    return realAreaTiles.size
}

fun findAreaTiles(nodes: List<List<Node>>, loop: List<Node>, firstInLoop: Pair<Int, Int>): Set<Node> {
    val areaTiles = mutableSetOf<Node>()
    val queue = mutableListOf(nodes[firstInLoop.first][firstInLoop.second])
    while (queue.isNotEmpty()) {
        val node = queue.removeFirst()
        areaTiles.add(node)
        val newAreaTiles = neighbourCoordinates().filter { (y, x) ->
            nodes.getOrNull(node.y + y)?.getOrNull(node.x + x).let {
                it != null && !areaTiles.contains(it) && !loop.contains(it) && !queue.contains(it)
            }
        }.map { (y, x) -> nodes[node.y + y][node.x + x] }
        queue.addAll(newAreaTiles)
    }
    return areaTiles
}

fun findFirstInLoop(loop: List<Node>): List<Pair<Int, Int>> {
    val start = loop.first()
    val first = loop[1]
    val end = loop[loop.lastIndex]
    val y = listOf(first, end).first { it.y != start.y }.y
    val x = listOf(first, end).first { it.x != start.x }.x
    val diffY = y - start.y
    val diffX = x - start.x
    return listOf(start.y + diffY to start.x + diffX, start.y + diffY to start.x - diffX)
}

private fun doubleNodes(nodes: List<List<Node>>): List<List<Node>> {
    val doubleNodes = (0..<nodes.size * 2).map { y ->
        (0..<nodes[0].size * 2).map { x ->
            if (y % 2 == 0 && x % 2 == 0) Node(x, y, nodes[y / 2][x / 2].pipe)
            else Node(x, y, ' ')
        }
    }
    return doubleNodes.mapIndexed { y, row ->
        row.mapIndexed { x, node ->
            if (y % 2 == 0) {
                if (x == 0 || x >= row.lastIndex) node
                else if (row[x - 1].pipe == '-' || row[x + 1].pipe == '-') {
                    Node(x, y, '-')
                } else if (row[x - 1].pipe == 'F' && setOf('7', 'J', 'S').contains(row[x + 1].pipe)) {
                    Node(x, y, '-')
                } else if (row[x - 1].pipe == 'L' && setOf('7', 'J', 'S').contains(row[x + 1].pipe)) {
                    Node(x, y, '-')
                } else if (row[x - 1].pipe == 'S' && setOf('7', 'J').contains(row[x + 1].pipe)) {
                    Node(x, y, '-')
                } else node
            } else {
                if (doubleNodes[y - 1][x].pipe == '|' || y < doubleNodes.lastIndex && doubleNodes[y + 1][x].pipe == '|') {
                    Node(x, y, '|')
                } else if (doubleNodes[y - 1][x].pipe == '7' && y < doubleNodes.lastIndex && setOf('J', 'L', 'S').contains(doubleNodes[y + 1][x].pipe)) {
                    Node(x, y, '|')
                } else if (doubleNodes[y - 1][x].pipe == 'F' && y < doubleNodes.lastIndex && setOf('J', 'L', 'S').contains(doubleNodes[y + 1][x].pipe)) {
                    Node(x, y, '|')
                } else if (doubleNodes[y - 1][x].pipe == 'S' && y < doubleNodes.lastIndex && setOf('J', 'L').contains(doubleNodes[y + 1][x].pipe)) {
                    Node(x, y, '|')
                } else node

            }
        }
    }
}

private fun neighbourCoordinates(): List<Pair<Int, Int>> = listOf(1 to 0, -1 to 0, 0 to 1, 0 to -1)

private fun print(nodes: List<List<Node>>, loop: List<Node>, realAreaTiles: List<Node>) {
    val areaColor = "\u001b[31m" // red
    val loopColor = "\u001b[34m" // blue
    val reset = "\u001b[0m"
    nodes.forEach { line ->
        line.forEach { n ->
            if (loop.contains(n)) print("$loopColor${n.pipe}$reset")
            else if (realAreaTiles.contains(n)) print("${areaColor}${n.pipe}$reset")
            else print(n.pipe)
        }
        println()
    }
}