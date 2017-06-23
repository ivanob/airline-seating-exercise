import scala.io.Source

object Main {
  def main(args: Array[String]): Unit = {
    val c = readContraintsFromFile("assets/input.txt")
    val best = GeneticAlgorithm.runGeneticAlgorithm(2000, 40, 100, 0.01f, c)
    println(best)
    /*val c: Constraints = new Constraints(4, 4,
      List(List("1W"),List("2W"),List("3W"),List("4W"),List("5W"),List("6W"),List("7","8","9")))
    val best = GeneticAlgorithm.runGeneticAlgorithm(500, 40, 100, 0.01f, c)
    println(best)*/
  }

  def readContraintsFromFile(filePath : String): Constraints ={
    var numCols = 0
    var numRows = 0
    var listGroups: List[List[String]] = List()
    for ((line,count) <- Source.fromFile(filePath).getLines().zipWithIndex) {
      if(count==0) {
        numRows = line.split(" ")(0).toInt
        numCols = line.split(" ")(1).toInt
      }
      else
        listGroups ::= line.split(" ").toList
    }
    new Constraints(numRows, numCols, listGroups)
  }
}