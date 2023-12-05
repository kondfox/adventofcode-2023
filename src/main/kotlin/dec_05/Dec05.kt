package dec_05

/* Part 1 */

fun part1(input: List<String>): Long = parseResourceConverters(input.drop(1)).let { converters ->
    return parseSeeds(input[0]).minOf {
            converters.fold(it) { num, converter -> converter.convert(num) }
    }
}

private fun parseResourceConverters(input: List<String>): List<ResourceConverter> = input
    .filter { it.isNotBlank() }
    .map(::parseConverter).fold(mutableListOf()) { resourceConverters, converter ->
        when (converter) {
            null -> resourceConverters.add(ResourceConverter())
            else -> resourceConverters.last().converters.add(converter)
        }
        resourceConverters
    }

private fun parseSeeds(input: String) = input.split(": ")[1]
    .split(" ")
    .map { it.trim().toLong() }

private fun parseConverter(line: String): Converter? {
    if (Regex("[^0-9]+").matches(line)) return null
    val (destinationStart, sourceStart, range) = line.split(" ")
    return Converter(destinationStart, sourceStart, range)
}

data class ResourceConverter(val converters: MutableList<Converter> = mutableListOf()) {
    fun convert(num: Long) = converters.firstOrNull { it.isInRange(num) }?.convert(num) ?: num
    fun convert(range: LongRange) = converters.firstOrNull { it.isInRange(range.first) }?.convert(range) ?: range
}

data class Converter(val destinationStart: String, val sourceStart: String, val range: String) {
    fun isInRange(num: Long) = num >= sourceStart.toLong() && num < (sourceStart.toLong() + range.toLong())
    fun convert(num: Long) = num + (destinationStart.toLong() - sourceStart.toLong())
    fun convert(range: LongRange) = convert(range.first)..convert(range.last)
    fun range() = sourceStart.toLong()..< (sourceStart.toLong() + range.toLong())
}

/* Part 2 */

fun part2(input: List<String>): Long = parseResourceConverters(input.drop(1)).let { resourceConverters ->
    return resourceConverters.fold(parseSeedRanges(input[0])) { ranges, resourceConverter ->
        ranges.flatMap { seedRange ->
            splitRange(seedRange, findIntersections(seedRange, resourceConverter)).map {
                resourceConverter.convert(it)
            }
        }
    }.minOf { it.first }
}

private fun parseSeedRanges(input: String) = input.split(": ")[1]
    .split(" ")
    .map { it.trim().toLong() }
    .windowed(2, 2)
    .map { it[0]..< it[0] + it[1] }

private fun splitRange(range: LongRange, intersections: Set<LongRange>): List<LongRange> {
    return listOf(*intersections.toTypedArray(), range)
        .flatMap { listOf(it.first, it.last) }
        .asSequence()
        .distinct()
        .sorted()
        .windowed(2)
        .map { it[0]..it[1] }
        .toList()
        .let { splitted ->
            splitted.mapIndexed { i, it ->
                when {
                    it in intersections -> it
                    i == 0 -> it.first..<it.last
                    i == splitted.lastIndex -> (it.first + 1)..it.last
                    else -> (it.first + 1)..<it.last
                }
            }.filter { it.first <= it.last }
        }
}

private fun findIntersections(seedRange: LongRange, resourceConverter: ResourceConverter): Set<LongRange> =
    resourceConverter.converters.mapNotNull { seedRange.intersect(it.range()) }.toSet()

private fun LongRange.intersect(other: LongRange): LongRange? {
    if (this.last < other.first || this.first > other.last) return null
    val start = if (this.first > other.first) this.first else other.first
    val end = if (this.last < other.last) this.last else other.last
    return start..end
}