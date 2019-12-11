import java.io.File

open class Day(date: Int ){
    internal val input = File("input-$date.txt").readText().removeSuffix("\n")

    fun input() = input
    fun inputLines() = input.lines()
    fun inputInts() = input.lines().map { it.toInts() }

}