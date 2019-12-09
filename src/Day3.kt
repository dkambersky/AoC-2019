import java.io.File
import java.lang.IllegalArgumentException
import kotlin.math.abs

private val input = File("input-3.txt").readLines()

fun main() {
    val (fst, snd) = input
        .map { it.split(",") }
        .map { Wire.fromStr(it) }

    val candidates = fst.path.toSet() intersect snd.path.toSet()
    print(candidates.minBy { it.distanceTo() }?.distanceTo())
}

data class Wire(val path: MutableList<Point> = mutableListOf()) {

    companion object {
        fun fromStr(turns: List<String>): Wire {
            val path = mutableListOf<Point>()

            turns.fold(Point()) { current, turn ->
                val (direction, length) = turn.first() to turn.drop(1).toInt()
                for (it in 1..length)
                    path.add(current.inDirection(direction, it))

                path.last()
            }

            return Wire(path)
        }
    }

    data class Point(val x: Int = 0, val y: Int = 0) {
        fun distanceTo(other: Point = Point()) =
            abs(x - other.x) + abs(y - other.y)

        fun inDirection(direction: Char, displacement: Int = 1) = when (direction) {
            'R' -> Point(x + displacement, y)
            'L' -> Point(x - displacement, y)
            'D' -> Point(x, y - displacement)
            'U' -> Point(x, y + displacement)
            else -> throw IllegalArgumentException("Provide a proper direction!")
        }


        override fun toString(): String {
            return "[$x,$y]"
        }

    }
}