import java.io.File

private val input = File("input-8.txt").readText().toInts()
fun main() {
    val (width, height) = 25 to 6

    val layers = input.chunked(width * height)
    val part1 = layers.minBy { it.count { it == 0 } }?.let {
        it.count { it == 1 } * it.count { it == 2 }
    } ?: -1
    println(part1)

    val part2 = layers.foldRight(layers.last()) { acc, next ->
        acc.zip(next).map { (new, old) -> if (new == 2) old else new }
    }
    println(part2.prettyPrintStr(width))
}


fun String.toInts() = filter { it.isDigit() }.map { it.toString().toInt() }
fun List<Int>.prettyPrintStr(width: Int) = chunked(width)
    .joinToString("\n") { it.joinToString("") }
    .replace('1', '\u2588')
    .replace('0', ' ')