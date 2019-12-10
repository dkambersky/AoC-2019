import java.io.File
import java.lang.Math.PI

private val input = File("input-10.txt").readText()
private val input2 = """.#..##.###...#######
##.############..##.
.#.######.########.#
.###.#######.####.#.
#####.##.#.##.###.##
..#####..#.#########
####################
#.####....###.#.#.##
##.#################
#####.##.###..####..
..######..##.#######
####.##.####...##..#
.#####..#.######.###
##...#.##########...
#.##########.#######
.####.#.###.###.#.##
....##.##.###..#####
.#.#.###########.###
#.#.#.#####.####.###
###.##.####.##.#..##"""

fun main() {
    val asteroids = input.lines().mapIndexed { y, xs ->
        xs.mapIndexed { x, c -> if (c == '#') Point(x, y) else null }
    }.flatten().filterNotNull().toMutableList()

    val station = asteroids.maxBy { visibleFrom(it, asteroids).size }!!
    println(visibleFrom(station, asteroids).size)  // Part 1

    val a = Point(5,5)
    val b = Point(5, 4)
    val c = Point(6,5)
    val d = Point(5,6)
    val e = Point(4,5)
    val f = Point(4,4)


//    print("EXPECTING 0 ")
//    println(a.angle(b))
//
//    print("EXPECTING 90 ")
//    println(a.angle(c))
//
//    print("EXPECTING 180 ")
//    println(a.angle(d))
//
//    print("EXPECTING 270 ")
//    println(a.angle(e))
//
//    print("EXPECTING 315 ")
//    println(a.angle(f))




    var i = 0
    while (i <= 199) {
        val visible = visibleFrom(station, asteroids)

        println("Beginning rotation. station $station, first:  ${visible.entries.first()}")


        for ((_, candidates) in visible.entries) {
            if(i == 199) {
                println(candidates.first())
                i++
                break
            }
            println("Nuking $i -> ${candidates.first()}")
            asteroids.remove(candidates.first())
            i++
        }
    }



}

fun visibleFrom(asteroid: Point, asteroids: List<Point>) =
    asteroids
        .filterNot { it == asteroid }
        .groupBy { asteroid.angle(it)  }
        .map { it.key to it.value.sortedBy { asteroid.distance(it) } }
        .toMap().toSortedMap()