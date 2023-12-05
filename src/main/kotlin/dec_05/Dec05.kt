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
}

data class Converter(val destinationStart: String, val sourceStart: String, val range: String) {
    fun isInRange(num: Long) = num >= sourceStart.toLong() && num < (sourceStart.toLong() + range.toLong())
    fun convert(num: Long) = num + (destinationStart.toLong() - sourceStart.toLong())
}