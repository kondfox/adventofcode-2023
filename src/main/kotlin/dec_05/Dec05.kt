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
    val seedRanges = parseSeedRanges(input[0])
    return resourceConverters.fold(seedRanges) { ranges, resourceConverter ->
        ranges.flatMap { seedRange ->
            val intersections = findIntersections(seedRange, resourceConverter)
            val splitted = splitRange(seedRange, intersections)
            val converted = splitted.map { resourceConverter.convert(it) }
            converted
        }
    }.map { it.first }.min()
}

fun findIntersections(seedRange: LongRange, resourceConverter: ResourceConverter): List<LongRange> =
    resourceConverter.converters.mapNotNull { seedRange.intersect(it.range()) }

fun splitRange(range: LongRange, intersections: List<LongRange>): List<LongRange> {
    if (intersections.isEmpty()) return listOf(range)
    val sortedInteractions = intersections.sortedWith(compareBy({ it.first }, { it.last }))
    val splitted = mutableListOf<LongRange>()
    if (range.first < sortedInteractions.first().first) {
        splitted.add(range.first..sortedInteractions.first().first)
    }
    sortedInteractions.fold(splitted) { subRanges, intersection ->
        if (subRanges.isEmpty()) {
            subRanges.add(intersection)
        } else {
            if (subRanges.last().last == intersection.first - 1) {
                subRanges.add(intersection)
            } else {
                subRanges.add(subRanges.last().last + 1..intersection.first)
            }
        }
        subRanges
    }
    if (range.last > sortedInteractions.last().last) {
        splitted.add(sortedInteractions.last().last + 1..range.last)
    }
    return splitted
}

private fun parseSeedRanges(input: String) = input.split(": ")[1]
    .split(" ")
    .map { it.trim().toLong() }
    .windowed(2, 2)
    .map { it[0]..< it[0] + it[1] }

private fun LongRange.intersect(other: LongRange): LongRange? {
    if (this.last < other.first || this.first > other.last) return null
    val start = if (this.first > other.first) this.first else other.first
    val end = if (this.last < other.last) this.last else other.last
    return start..end
}