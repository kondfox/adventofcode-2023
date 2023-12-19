package dec_19

/* Part 1 */

fun part1(input: List<String>): Int {
    val (rules, parts) = parse(input)
    return parts.map { rules["in"]!!.evaluate(it, rules) }
        .filter { it.first == "A" }
        .sumOf { it.second.sum() }
}

private fun parse(input: List<String>): Pair<Map<String, Rule>, List<Part>> {
    val rules = input.takeWhile { it.isNotEmpty() }.map { parseRule(it) }
    val parts = input.dropWhile { it.isNotEmpty() }.drop(1).map { parsePart(it) }
    return rules.associateBy { it.name } to parts
}

private fun parseRule(line: String): Rule {
    val (name, conditionSting) = Regex("([a-z]+)\\{(.*)}").find(line)!!.destructured
    val conditions = conditionSting.split(",").map { parseCondition(it) }
    return Rule(name, conditions)
}

fun parsePart(part: String): Part {
    val (x, m, a, s) = Regex("\\{x=([0-9]+),m=([0-9]+),a=([0-9]+),s=([0-9]+)}").find(part)!!.destructured
    return Part(x.toInt(), m.toInt(), a.toInt(), s.toInt())
}

fun parseCondition(condition: String): Condition {
    if (condition.contains(":")) {
        val (attr, comparison, value, workflow) = Regex("([a-z]+)([<>])([0-9]+):(\\w+)").find(condition)!!.destructured
        return Condition(attr, comparison, value.toInt(), workflow)
    } else {
        return Condition(null, null, null, condition)
    }
}

data class Rule(val name: String, val conditions: List<Condition>) {
    fun evaluate(part: Part, rules: Map<String, Rule>): Pair<String, Part> {
        val workflow = conditions.firstNotNullOf { it.evaluate(part) }
        return if (workflow == "A" || workflow == "R") {
            workflow to part
        } else {
            rules[workflow]!!.evaluate(part, rules)
        }
    }
}

data class Condition(val attr: String?, val comparison: String?, val value: Int?, val workflow: String) {
    fun evaluate(part: Part): String? {
        if (attr == null || comparison == null || value == null) return workflow
        return when (comparison) {
            ">" -> if (part[attr] > value) workflow else null
            "<" -> if (part[attr] < value) workflow else null
            else -> null
        }
    }
}

data class Part(val x: Int, val m: Int, val a: Int, val s: Int) {
    operator fun get(attr: String): Int {
        return this::class.java.getDeclaredField(attr).getInt(this)
    }

    fun sum(): Int = x + m + a + s
}
