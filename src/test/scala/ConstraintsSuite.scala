import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest._

@RunWith(classOf[JUnitRunner])
class ConstraintsSuite extends FunSuite {

  test("test of groupsMap structure"){
    val c:Constraints = new Constraints(2,4,
      List(List("1W","2W","3"),List("4W","5"),List("6W","7","8W")))
    //Testing first group
    assert(c.groupsMap.contains(1) && c.groupsMap.contains(2) && c.groupsMap.contains(3))
    assert(c.groupsMap(1) == List(1,2,3))
    assert(c.groupsMap(2) == c.groupsMap(1))
    assert(c.groupsMap(3) == c.groupsMap(1))
    //Testing second group
    assert(c.groupsMap.contains(4) && c.groupsMap.contains(5))
    assert(c.groupsMap(4) == List(4,5))
    assert(c.groupsMap(4) == c.groupsMap(5))
    //Testing third group
    assert(c.groupsMap.contains(6) && c.groupsMap.contains(7) && c.groupsMap.contains(8))
    assert(c.groupsMap(6) == List(6,7,8))
    assert(c.groupsMap(7) == c.groupsMap(6))
    assert(c.groupsMap(8) == c.groupsMap(6))
  }

  test("Test windowCustomers structure"){
    val c:Constraints = new Constraints(2,4,
      List(List("1W","2W","3"),List("4W","5"),List("6W","7","8W")))
    assert(c.windowCustomers == Set(1,2,4,6,8))
  }

  test("Test passengers structure"){
    { //Same number of passengers than seats
      val c: Constraints = new Constraints(2, 4,
        List(List("1W", "2W", "3"), List("4W", "5"), List("6W", "7", "8W")))
      val expectedArray = Array(1, 2, 3, 4, 5, 6, 7, 8)
      assert(c.passengers.length == 8)
      assert(c.passengers.deep == expectedArray.deep)
    }
    { //Less number of passengers than seats (need to fill some spots with 0s)
      val c: Constraints = new Constraints(2, 4,
        List(List("1W", "2W", "3"), List("4W", "5")))
      val expectedArray = Array(1, 2, 3, 4, 5, 0, 0, 0)
      assert(c.passengers.length == 8)
      assert(c.passengers.deep == expectedArray.deep)
    }
    { //Overbooking: more number of passengers than seats
      val c: Constraints = new Constraints(2, 4,
        List(List("1W", "2W", "3"), List("4W", "5"), List("6W", "7", "8W","9","10","11")))
      val expectedArray = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
      assert(c.passengers.length == 11)
      assert(c.passengers.deep == expectedArray.deep)
    }
  }

  test("Test createGroupsMapSimplified() function"){
    val c: Constraints = new Constraints(2, 4,
      List(List("1W", "2W", "3"), List("4W", "5"), List("6W", "7", "8W")))
    //Valid keys
    assert(c.groupsMapSimplified.contains(1))
    assert(c.groupsMapSimplified.contains(4))
    assert(c.groupsMapSimplified.contains(6))
    //Keys we dont expect
    assert(!c.groupsMapSimplified.contains(2))
    assert(!c.groupsMapSimplified.contains(3))
    assert(!c.groupsMapSimplified.contains(5))
    assert(!c.groupsMapSimplified.contains(7))
    assert(!c.groupsMapSimplified.contains(8))
  }

}
