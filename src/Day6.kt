import java.io.File

private val input = File("input-6.txt").readLines()

fun main() {
    val edges = input.map { it.split(")") }.associate { toks -> toks[1] to toks[0] }

    fun path(from: String): MutableSet<String> =
        if (from == "COM") mutableSetOf("COM")
        else path(edges[from]!!).apply { add(from) }

    val sum = edges.keys.sumBy { path(it).size - 1 }
    val path = path("SAN") symmetricDifference path("YOU")


    println(sum)
    println(path.size - 2)
}

infix fun <T> Iterable<T>.symmetricDifference(other: Iterable<T>): Set<T> =
    (this union other) subtract (this intersect other)
