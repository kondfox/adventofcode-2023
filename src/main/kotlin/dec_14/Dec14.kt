package dec_14

fun part1(input: List<String>): Int = tiltUp(parse(input)).calculateLoad()

fun part2(input: List<String>): Int {
    val (repeatingFrom, firstSame) = findRepetition(parse(input))
    val repetition = firstSame - repeatingFrom
    val dish = parse(input)
    (0..<(repeatingFrom + (1_000_000_000 - firstSame) % repetition)).forEach { cycle(dish) }
    return dish.calculateLoad()
}

private fun parse(input: List<String>): List<CharArray> = input.map { it.toCharArray() }

private fun cycle(dish: List<CharArray>): String {
    return listOf(::tiltUp, ::tiltLeft, ::tiltDown, ::tiltRight)
        .fold(dish) { _, tilt -> tilt(dish) }
        .asString()
}

private fun findRepetition(dish: List<CharArray>): Pair<Int, Int> {
    val variations: MutableList<String> = mutableListOf()
    var repeatingFrom = 0
    var i = 0
    while (i < 1_000_000_000) {
        val state = cycle(dish)
        val prev = variations.indexOf(state)
        if (prev > -1) {
            repeatingFrom = prev
            break
        } else {
            variations.add(state)
        }
        i++
    }
    return repeatingFrom to i
}

private fun List<CharArray>.calculateLoad(): Int =
    this.mapIndexed { i, row -> row.count { it == 'O' } * (this.size - i) }.sum()

private fun tiltUp(dish: List<CharArray>): List<CharArray> {
    for (col in dish[0].indices) {
        var next = 0
        for (row in dish.indices) {
            if (dish[row][col] == 'O') {
                if (row != next) {
                    dish[row][col] = '.'
                }
                dish[next++][col] = 'O'
            } else if (dish[row][col] == '#') {
                next = row + 1
            }
        }
    }
    return dish
}

private fun tiltDown(dish: List<CharArray>): List<CharArray> {
    for (col in dish[0].indices) {
        var next = dish.lastIndex
        for (row in dish.indices.reversed()) {
            if (dish[row][col] == 'O') {
                if (row != next) {
                    dish[row][col] = '.'
                }
                dish[next--][col] = 'O'
            } else if (dish[row][col] == '#') {
                next = row - 1
            }
        }
    }
    return dish
}

private fun tiltLeft(dish: List<CharArray>): List<CharArray> {
    for (row in dish.indices) {
        var next = 0
        for (col in dish[0].indices) {
            if (dish[row][col] == 'O') {
                if (col != next) {
                    dish[row][col] = '.'
                }
                dish[row][next++] = 'O'
            } else if (dish[row][col] == '#') {
                next = col + 1
            }
        }
    }
    return dish
}

private fun tiltRight(dish: List<CharArray>): List<CharArray> {
    for (row in dish.indices) {
        var next = dish[0].lastIndex
        for (col in dish[0].indices.reversed()) {
            if (dish[row][col] == 'O') {
                if (col != next) {
                    dish[row][col] = '.'
                }
                dish[row][next--] = 'O'
            } else if (dish[row][col] == '#') {
                next = col - 1
            }
        }
    }
    return dish
}

private fun List<CharArray>.asString(): String = this.joinToString("") { it.joinToString("") }