fun main() {
    val input = 171309 to 643603

    val valid = (input.first..input.second)
        .map { it.toString().map { i -> i.toInt() } }
        .filter { firstConstraint(it) }

    val validUnderSecond = valid.filter { secondConstraint(it) }

    println(valid.size)
    println(validUnderSecond.size)
}

private fun firstConstraint(numbers: List<Int>): Boolean {
    val doublesValid = numbers
        .zipWithNext()
        .any { it.first == it.second }

    val ascendingValid = numbers == numbers.sorted()
    return doublesValid && ascendingValid
}

private fun secondConstraint(numbers: List<Int>) =
    (2 in numbers.groupingBy { it }.eachCount().values)