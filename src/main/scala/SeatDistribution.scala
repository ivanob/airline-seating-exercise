
class SeatDistribution(c:Constraints, fixedSeats: Option[Array[Array[Int]]] = None){
  //In case the fixedSeats attribute is not provided, it will generate a random distribution of seats
  val seats: Array[Array[Int]] = fixedSeats match {
    case Some(s) => correctDistribution(s)
    case None => generateRandomDistribution(c: Constraints)}
  val fitness: Float = calculateFitness()

  def findPassengersWithoutSeat(seatedPassengers: Array[Int]): Array[Int] = {
    c.passengers diff seatedPassengers
  }

  def correctDistribution(distr: Array[Array[Int]]): Array[Array[Int]] = {
    val array = distr.flatten
    var passengersWithoutSeat = findPassengersWithoutSeat(array)
    val repeatedPassengers = array.groupBy(identity).collect { case (x, Array(_,_,_*)) => x }.toList
    repeatedPassengers.foreach(r => {
      val pair = dropElement(passengersWithoutSeat, None)
      passengersWithoutSeat = pair._1
      array(array.indexOf(r)) = pair._2
    })
    array.grouped(c.cols).map(_.toArray).toArray
  }

  def generateRandomDistribution(c:Constraints) = {
    var passengersToSeat = c.passengers
    val r = scala.util.Random
    Array.tabulate(c.rows,c.cols)((_,_) => {
      val dropped = dropElement(passengersToSeat, None)
      passengersToSeat = dropped._1
      dropped._2
    })
  }

  def isWindowSatisfied(customer: Int, colPassengerSeats: Int): Boolean = {
    if(!c.windowCustomers.contains(customer)) true
    else (c.windowCustomers.contains(customer) && (colPassengerSeats==0 || colPassengerSeats==c.cols-1))
  }

  def isPassengerHappy(): Boolean = ???

  def isGroupTogether(group: Array[Int], row: Array[Int]): Boolean = {
    val seated = row.intersect(group)
    if(seated.length != group.length) false //The group is not complete
    else {
      val arrayPositions = seated.map(x => row.indexWhere(y => x == y))
      val minPosition = arrayPositions.reduceLeft(_ min _)
      val maxPosition = arrayPositions.reduceLeft(_ max _)
      //Check if all the passengers are in consecutive order
      (maxPosition - minPosition == group.length - 1)
    }
  }

  def isGroupHappy(passenger: Int): Boolean = {
    findRowOfCustomer(passenger) match{
      case Some(row) => {
        isGroupTogether(c.groupsMap(passenger).toArray, row) &&
          c.groupsMap(passenger).forall(passenger => isWindowSatisfied(passenger, row.indexWhere(_ == passenger)))
      }
      case None => false
    }
  }

  def findRowOfCustomer(passenger: Int): Option[Array[Int]] = {
    val index = seats.indexWhere((array: Array[Int]) => array.contains(passenger))
    if(index != -1) Some(seats(index))
    else None
  }

  def countNumberHappyPassengers() = {
    val happyGroups = c.groupsMapSimplified.filter((group: (Int, List[Int])) => isGroupHappy(group._1)).toList
    if(!happyGroups.isEmpty) happyGroups.map(x=>x._2.length).reduce(_ + _)
    else 0
  }

  def calculateFitness(): Float = {
    countNumberHappyPassengers().toFloat/c.capacityPlane
  }

  def dropElement(arr: Array[Int], pos: Option[Int]): (Array[Int], Int) = {
    arr match {
      case Array() => (Array(),0)
      case _ => {
        val index = pos match {case Some(x) => x case None => scala.util.Random.nextInt(arr.length)}
        ((arr.view.take(index) ++ arr.view.drop(index + 1)).toArray, arr(index))
      }
    }
  }

  override def toString: String = {
    val str: String = (seats.map(_.mkString(" ")).mkString("\n"))
    val finalFitness = fitness*100
    str + s"\n$finalFitness%"
  }
}
