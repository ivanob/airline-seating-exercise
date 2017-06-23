import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

class GeneticAlgorithmSuite extends FunSuite {

  test("test initialization(...) function"){
    val c:Constraints = new Constraints(2,2,
      List(List("1","2"),List("3","4")))
    val population = GeneticAlgorithm.initialization(100, c)
    assert(population.length == 100)
  }

  test("test orderPopulation(...) function"){
    val c:Constraints = new Constraints(2,2,
      List(List("1","2"),List("3","4")))
    val population = GeneticAlgorithm.initialization(100, c)
    val orderedPopulation = GeneticAlgorithm.orderPopulation(population)
    //TODO compare
  }

  test("test swapGenes(...) function"){
    {
      val chromosome1 = List(0,0,0,0,0,0)
      val chromosome2 = List(1,1,1,1,1,1)
      val children = GeneticAlgorithm.swapGenes((chromosome1, chromosome2), 3)
      assert(children._1==List(0,0,0,1,1,1))
      assert(children._2==List(1,1,1,0,0,0))
    }
    {
      val chromosome1 = List(0,0,0,0,0,0)
      val chromosome2 = List(1,1,1,1,1,1)
      val children = GeneticAlgorithm.swapGenes((chromosome1, chromosome2), 1)
      assert(children._1==List(0,1,1,1,1,1))
      assert(children._2==List(1,0,0,0,0,0))
    }
    {
      val chromosome1 = List(0,0,0,0,0,0)
      val chromosome2 = List(1,1,1,1,1,1)
      val children = GeneticAlgorithm.swapGenes((chromosome1, chromosome2), 5)
      assert(children._1==List(0,0,0,0,0,1))
      assert(children._2==List(1,1,1,1,1,0))
    }
  }

  test("test crossover(...) function"){
    val c:Constraints = new Constraints(2,3,
      List(List("1","2","3"),List("4","5","6")))
    val distr1 = new SeatDistribution(c)
    val distr2 = new SeatDistribution(c)
    val distr3 = new SeatDistribution(c)
    val children = GeneticAlgorithm.crossover(List(distr1, distr2, distr3), 10, c)
    assert(children.length == 10)
  }

  test("test runGeneticAlgorithm(...) function"){
    val c: Constraints = new Constraints(4, 4,
      List(List("1W", "2", "3"),
        List("4", "5", "6", "7"),
        List("8"),
        List("9", "10", "11W"),
        List("12W"),
        List("13", "14"),
        List("15", "16")))
    val best = GeneticAlgorithm.runGeneticAlgorithm(100, 10, 100, 0.01f, c)
  }
}
