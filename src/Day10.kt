import java.io.File

private val input = File("input-10.txt").readText()

fun main() {
    val asteroids = input.lines().mapIndexed { y, xs ->
        xs.mapIndexed { x, c -> if (c == '#') Point(x, y) else null }
    }.flatten().filterNotNull().toMutableList()

    val station = asteroids.maxBy { visibleFrom(it, asteroids).size }!!
    println(visibleFrom(station, asteroids).size)  // Part 1

    var i = 0
    outer@while (true) {
        val visible = visibleFrom(station, asteroids)
        for ((_, candidates) in visible.entries) {
            if (i == 199) {
                println(candidates.first().let { it.x * 100 + it.y }) // Part 2
                break@outer
            }
            asteroids.remove(candidates.first())
            i++
        }
    }
}

fun visibleFrom(asteroid: Point, asteroids: List<Point>) =
    asteroids
        .filterNot { it == asteroid }
        .groupBy { asteroid.angle(it) }
        .map { it.key to it.value.sortedBy { asteroid.distance(it) } }
        .toMap().toSortedMap()