import java.io.File
import java.lang.IllegalArgumentException
import java.lang.Math.toDegrees
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2

private val input = File("input-3.txt").readLines()

fun main() {
    val (fst, snd) = input
        .map { it.split(",") }
        .map { Wire.fromStr(it) }

    val candidates = fst.path.toSet() intersect snd.path.toSet()

    val pairs = candidates.map { cand ->
        fst.path.first { it == cand } to snd.path.first { it == cand }
    }

    println(candidates.minBy { it.distance() }?.distance())
    println(pairs.minBy { it.combinedCosts() }?.combinedCosts())
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


}

data class Point(
    val x: Int = 0,
    val y: Int = 0,
    val cost: Int = 0
) {
    fun distance(other: Point = Point()) =
        abs(x - other.x) + abs(y - other.y)

    fun inDirection(direction: Char, displacement: Int = 1) = when (direction) {
        'R' -> Point(x + displacement, y, cost + displacement)
        'L' -> Point(x - displacement, y, cost + displacement)
        'D' -> Point(x, y - displacement, cost + displacement)
        'U' -> Point(x, y + displacement, cost + displacement)
        else -> throw IllegalArgumentException("Provide a proper direction!")
    }

    override fun equals(other: Any?): Boolean =
        if (other !is Point) false
        else x == other.x && y == other.y


    override fun toString(): String {
        return "[$x,$y]"
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    fun angle(other: Point): Double {
        return (toDegrees(atan2(other.y -y , other.x - x)) + 90).let {
            if (it < 0) it + 360 else it
        }
    }

}


fun atan2(y: Int, x: Int) = atan2(y.toDouble(), x.toDouble())
fun Pair<Point, Point>.combinedCosts() = first.cost + second.cost