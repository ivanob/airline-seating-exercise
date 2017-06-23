import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SeatDistributionSuite extends FunSuite {

  test("test isWindowSatisfied function"){
    val c: Constraints = new Constraints(2, 4,
      List(List("1W", "2W", "3"), List("4W", "5"), List("6W", "7", "8W")))
    val seatDistr: SeatDistribution = new SeatDistribution(c)
    //Seats where a window preference passenger is happy
    assert(seatDistr.isWindowSatisfied(1,0))
    assert(seatDistr.isWindowSatisfied(1,3))
    //Seats where a passenger without window preference is happy
    assert(seatDistr.isWindowSatisfied(3,0))
    assert(seatDistr.isWindowSatisfied(3,1))
    assert(seatDistr.isWindowSatisfied(3,2))
    assert(seatDistr.isWindowSatisfied(3,3))
    //Seats where a window preference passenger is unhappy
    assert(!seatDistr.isWindowSatisfied(1,1))
    assert(!seatDistr.isWindowSatisfied(1,2))
  }

  test("test findRowOfCustomer function"){
    { //All of them are in the first row
      val c: Constraints = new Constraints(1, 4,
        List(List("1", "2"), List("3", "4")))
      val seatDistr: SeatDistribution = new SeatDistribution(c)
      assert(seatDistr.findRowOfCustomer(1) match {case Some(x)=> x.contains(1)})
      assert(seatDistr.findRowOfCustomer(1) match {case Some(x)=> x.contains(2)})
      assert(seatDistr.findRowOfCustomer(1) match {case Some(x)=> x.contains(3)})
      assert(seatDistr.findRowOfCustomer(1) match {case Some(x)=> x.contains(4)})
    }
    { //Each passenger is in a different row alone
      val c: Constraints = new Constraints(4, 1,
        List(List("1", "2"), List("3", "4")))
      val seatDistr: SeatDistribution = new SeatDistribution(c)
      assert(seatDistr.findRowOfCustomer(1) match {case Some(x)=> x.deep == Array(1).deep})
      assert(seatDistr.findRowOfCustomer(2) match {case Some(x)=> x.deep == Array(2).deep})
      assert(seatDistr.findRowOfCustomer(3) match {case Some(x)=> x.deep == Array(3).deep})
      assert(seatDistr.findRowOfCustomer(4) match {case Some(x)=> x.deep == Array(4).deep})
    }
    { //Non existing passengers
      val c: Constraints = new Constraints(4, 1,
        List(List("1", "2"), List("3", "4")))
      val seatDistr: SeatDistribution = new SeatDistribution(c)
      assert(seatDistr.findRowOfCustomer(5) match {case None => true})
      assert(seatDistr.findRowOfCustomer(0) match {case None => true})
    }
  }

  test("test isGroupTogether function"){
    val c: Constraints = new Constraints(2, 4,
      List(List("1W", "2W", "3"), List("4W", "5"), List("6W", "7", "8W")))
    val seatDistr: SeatDistribution = new SeatDistribution(c)
    //Positive expected
    assert(seatDistr.isGroupTogether(Array(1,2,3), Array(1,2,3)))
    assert(seatDistr.isGroupTogether(Array(1,2,3), Array(4,1,2,3,6)))
    //Negative expected
    assert(!seatDistr.isGroupTogether(Array(1,2,3), Array(1,99,2,3)))
    assert(!seatDistr.isGroupTogether(Array(1,2,3), Array(1,2,99,3)))
    assert(!seatDistr.isGroupTogether(Array(1,2,3), Array(1)))
    assert(!seatDistr.isGroupTogether(Array(1,2,3), Array(1,2)))
  }

  test("test isGroupHappy function"){
    {
      val c: Constraints = new Constraints(2, 4,
        List(List("1W", "2", "3"), List("4W", "5"), List("6", "7", "8W")))
      val seatDistr: SeatDistribution = new SeatDistribution(c, Some(Array(Array(1, 2, 3, 4), Array(5, 6, 7, 8))))
      assert(seatDistr.isGroupHappy(1))
      assert(seatDistr.isGroupHappy(6))
      assert(!seatDistr.isGroupHappy(4))
    }
    {
      val c: Constraints = new Constraints(2, 4,
        List(List("1W", "2", "3", "4"), List("5W"), List("6"), List("7", "8")))
      val seatDistr: SeatDistribution = new SeatDistribution(c, Some(Array(Array(1, 2, 3, 5), Array(4, 6, 7, 8))))
      assert(!seatDistr.isGroupHappy(1))
      assert(seatDistr.isGroupHappy(5))
      assert(seatDistr.isGroupHappy(6))
      assert(seatDistr.isGroupHappy(7))
    }
  }

  test("test countNumberHappyPassengers() function"){
    { //6 satisfied passenders: group1 and group3
      val c: Constraints = new Constraints(2, 4,
        List(List("1W", "2", "3"), List("4W", "5"), List("6", "7", "8W")))
      val seatDistr: SeatDistribution = new SeatDistribution(c, Some(Array(Array(1, 2, 3, 4), Array(5, 6, 7, 8))))
      assert(seatDistr.countNumberHappyPassengers() == 6)
    }
    { //4 satisfied passengers: group2 and group3
      val c: Constraints = new Constraints(2, 4,
        List(List("1", "2W", "3"), List("4W"), List("8W", "6", "7")))
      val seatDistr: SeatDistribution = new SeatDistribution(c, Some(Array(Array(1, 2, 3, 4), Array(5, 6, 7, 8))))
      assert(seatDistr.countNumberHappyPassengers() == 4)
    }
    { //There is only one passenger (who is happy)
      val c: Constraints = new Constraints(2, 4,
        List(List("1")))
      val seatDistr: SeatDistribution = new SeatDistribution(c, Some(Array(Array(0, 0, 1, 0), Array(0, 0, 0, 0))))
      assert(seatDistr.countNumberHappyPassengers() == 1)
    }
    { //The example from the exercise
      val c: Constraints = new Constraints(4, 4,
        List(List("1W", "2", "3"),
          List("4", "5", "6", "7"),
          List("8"),
          List("9", "10", "11W"),
          List("12W"),
          List("13", "14"),
          List("15", "16")))
      val seatDistr: SeatDistribution = new SeatDistribution(c, Some(Array(
        Array(1, 2, 3, 8),
        Array(4, 5, 6, 7),
        Array(11, 9, 10, 12),
        Array(13, 14, 15, 16))))
      assert(seatDistr.countNumberHappyPassengers() == 16)
    }
  }

  test("test calculateFitness() function"){
    { //6 satisfied passenders out of 8
      val c: Constraints = new Constraints(2, 4,
        List(List("1W", "2", "3"), List("4W", "5"), List("6", "7", "8W")))
      val seatDistr: SeatDistribution = new SeatDistribution(c, Some(Array(Array(1, 2, 3, 4), Array(5, 6, 7, 8))))
      assert(seatDistr.calculateFitness() == 0.75)
    }
    { //4 satisfied passengers out of 8
      val c: Constraints = new Constraints(2, 4,
        List(List("1", "2W", "3"), List("4W"), List("8W", "6", "7")))
      val seatDistr: SeatDistribution = new SeatDistribution(c, Some(Array(Array(1, 2, 3, 4), Array(5, 6, 7, 8))))
      assert(seatDistr.calculateFitness() == 0.5)
    }
    { //1 satisfied passenger out of 8
      val c: Constraints = new Constraints(2, 4,
        List(List("1")))
      val seatDistr: SeatDistribution = new SeatDistribution(c, Some(Array(Array(0, 0, 1, 0), Array(0, 0, 0, 0))))
      assert(seatDistr.calculateFitness() == 0.125)
    }
    { //The example from the exercise
      val c: Constraints = new Constraints(4, 4,
        List(List("1W", "2", "3"),
            List("4", "5", "6", "7"),
            List("8"),
            List("9", "10", "11W"),
            List("12W"),
            List("13", "14"),
            List("15", "16")))
      val seatDistr: SeatDistribution = new SeatDistribution(c, Some(Array(
        Array(1, 2, 3, 8),
        Array(4, 5, 6, 7),
        Array(11, 9, 10, 12),
        Array(13, 14, 15, 16))))
      assert(seatDistr.calculateFitness() == 1)
    }
    {
      val c: Constraints = new Constraints(4, 4,
        List(List("1W", "2", "3"),
          List("4", "5", "6", "7"),
          List("8"),
          List("9", "10", "11W"),
          List("12W"),
          List("13", "14"),
          List("15", "16")))
      val seatDistr: SeatDistribution = new SeatDistribution(c, Some(Array(
        Array(13, 14, 5, 12),
        Array(1, 8, 3, 10),
        Array(11, 7, 9, 2),
        Array(6, 16, 15, 4))))
      assert(seatDistr.calculateFitness() != 1)
    }
  }

  test("test correctDistribution(...) function"){
    {
      val c: Constraints = new Constraints(2, 2,
        List(List("1", "2", "3", "4")))
      val seatDistr: SeatDistribution = new SeatDistribution(c)
      val m = Array(Array(1, 1), Array(2, 3))
      assert(seatDistr.correctDistribution(m).flatten.toList==List(4,1,2,3))
    }
    {
      val c: Constraints = new Constraints(2, 2,
        List(List("1", "2", "3", "4")))
      val seatDistr: SeatDistribution = new SeatDistribution(c)
      val m = Array(Array(1, 2), Array(4, 4))
      assert(seatDistr.correctDistribution(m).flatten.toList==List(1,2,3,4))
    }
    {
      val c: Constraints = new Constraints(2, 2,
        List(List("1", "2")))
      val seatDistr: SeatDistribution = new SeatDistribution(c)
      val m = Array(Array(1, 0), Array(0, 2))
      assert(seatDistr.correctDistribution(m).flatten.toList==List(1,0,0,2))
    }
    {
      val c: Constraints = new Constraints(2, 2,
        List(List("1")))
      val seatDistr: SeatDistribution = new SeatDistribution(c)
      val m = Array(Array(1, 0), Array(1, 0))
      assert(seatDistr.correctDistribution(m).flatten.toList==List(0,0,1,0))
    }
  }

  test("test findPassengersWithoutSeat(...) function"){
    val c: Constraints = new Constraints(2, 2,
      List(List("1", "2", "3", "4")))
    val seatDistr: SeatDistribution = new SeatDistribution(c)
    val m = Array(Array(1,1),Array(2,3))
    assert(seatDistr.findPassengersWithoutSeat(m.flatten).deep==Array(4).deep)
  }
}
