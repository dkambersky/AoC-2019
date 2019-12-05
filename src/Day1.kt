import java.io.File

fun main() {
    val input = File("input-1.txt")
        .readLines()
        .map { it.toInt() }

    println(run(input))
    println(part2(input))
}

private fun run(input: List<Int>) = input.sumBy { it / 3 - 2 }

private fun part2(input: List<Int>) = input.sumBy {
    var cost = it / 3 - 2
    var cumSum = 0
    do {
        cumSum += cost
        cost = cost / 3 - 2
    } while (cost > 0)
    cumSum
}