//import java.io.File
//import kotlin.math.abs
//
//private val input = File("input-3.txt").readLines()
//
//fun main() {
//    val (fst, snd) = input
//        .map { it.split(",") }
//        .map { Wire.fromStr(it) }
//
//}
//
//data class Wire(val path: MutableList<Point> = mutableListOf()) {
//
//    companion object {
//        fun fromStr(turns: List<String>): Wire {
//            var position = Point()
//            turns.forEach {
//
//            }
//        }
//    }
//}
//
//data class Point(val x: Int = 0, val y: Int = 0) {
//    fun distanceTo(other: Point = Point()) =
//        abs(x - other.x) + abs(y - other.y)
//
//}