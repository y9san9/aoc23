
import me.y9san9.recycling.RecyclingList
import me.y9san9.recycling.RecyclingMode

fun main() {
    val list1 = RecyclingList(3, RecyclingMode.Fast) { it }
    val list2 = list1.drop(n = 1)
    val list3 = list2.drop(n = 1)

    require(list1 === list2)
    require(list2 === list3)
    require(list1 === list3)
}
