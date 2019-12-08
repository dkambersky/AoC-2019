import java.io.File

private val input = File("input-8.txt").readText().toInts()
fun main() {
    val (width, height) = 25 to 6
    val layers = input.chunked(width * height)
    val part1 = layers.minBy { it.count { it == 0 } }?.let {
        it.count { it == 1 } * it.count { it == 2 }
    } ?: -1
    println(part1)
}

fun String.toInts() = filter { it.isDigit() }.map { it.toString().toInt() }