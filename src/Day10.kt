import intcode.Machine
import java.io.File

private val input = File("input-10.txt").readText()
private val input2 = """.#..#
.....
#####
....#
...##"""

fun main() {

    val asteroids = mutableListOf<Asteroid>()
    val map = input.lines().mapIndexed { x, ys ->
        ys.mapIndexed inner@{ y, c ->
            if (c == '.') return@inner null
            Asteroid(x, y).apply {
                asteroids.add(this)
                return@inner this
            }
        }.filterNotNull()
    }.flatten()


    val a = asteroids.maxBy { visibleFrom(it, asteroids) } ?: throw Exception("WtF")
    println(a)
    print(visibleFrom(a, asteroids))


}

fun visibleFrom(asteroid: Asteroid, asteroids: List<Asteroid>): Int =
    chunked(asteroids, asteroid).sumBy{ it.groupBy { it.slope(asteroid) }.keys.count() }

fun chunked(asteroids: List<Asteroid>, around: Asteroid) =
    asteroids.groupBy { it.x > around.x }
        .values.map { it.groupBy { it.y > around.y } }
        .map { it.values }
        .flatten()

data class Asteroid(val x: Int, val y: Int) {
    fun slope(other: Asteroid) =
    (y - other.y) / (x - other.x)
        .let { if (it == 0) return Integer.MAX_VALUE - (if (y - other.y < 0) 1 else 2) else it }
}