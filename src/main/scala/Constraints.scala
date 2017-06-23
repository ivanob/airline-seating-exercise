
class Constraints(val rows:Int, val cols:Int, groups: List[List[String]]) {
  val capacityPlane = rows*cols
  val groupsMap = createGroupsMap()
  val groupsMapSimplified = createGroupsMapSimplified()
  val windowCustomers = selectWindowCustomers()
  val passengers: Array[Int] = generateListPassengers()
  type Groups = Map[Int, List[Int]] //Groups of passengers

  def generateListPassengers(): Array[Int] = {
    val numPassengers: Int = groups.flatten.length
    if(numPassengers < capacityPlane) groups.flatten.map(x => positionToInt(x)).toArray ++ Array.fill(capacityPlane-numPassengers)(0)
    else groups.flatten.map(x => positionToInt(x)).toArray
  }

  def selectWindowCustomers(): Set[Int] = {
    val l = for {
      l1 <- groups
      l2 <- l1
      if(isWindowPreference(l2))
    } yield (positionToInt(l2))
    l.toSet
  }

  def isWindowPreference(s: String): Boolean = s.takeRight(1).toLowerCase=="w"

  /**
    * Transform the description of each passenger into a number, so:
    * "1W" transforms into 1
    * "30" transforms into 30...
    * @param s
    * @return
    */
  def positionToInt(s: String): Int = {
    if(isWindowPreference(s)) s.dropRight(1).toInt
    else s.toInt
  }

  def createGroupsMap(): Groups = {
    groups.map(x => x.map(y => positionToInt(y) -> x.map(positionToInt(_)))).flatten.toMap
  }

  /**
    * As being happy or not is a binary condition for a group,
    * I only need a representation of the group to keep a reference
    * of the map. In other words, I dont need a key for each member
    * in the map
    * @return
    */
  def createGroupsMapSimplified(): Groups={
    groups.map(x => (positionToInt(x.head), x.map(y=>positionToInt(y)))).toMap
  }
}

object Constraints {
}
