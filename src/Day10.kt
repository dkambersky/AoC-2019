import java.io.File

private val input = File("input-10.txt").readText()

fun main() {

    val asteroids = mutableListOf<Point>()
    input.lines().forEachIndexed { x, ys ->
        ys.forEachIndexed { y, c ->
            if (c == '.') asteroids.add(Point(x, y))
        }
    }

    val best = asteroids.maxBy { visibleFrom(it, asteroids) }
    print(visibleFrom(best!!, asteroids))

}

fun visibleFrom(asteroid: Point, asteroids: List<Point>): Int =
    asteroids
        .filterNot { it == asteroid }
        .groupBy { it.angle(asteroid) }.keys.count()