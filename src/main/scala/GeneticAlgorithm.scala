
object GeneticAlgorithm {
  type Population = List[SeatDistribution]
  type Chromosome = List[Int]
  val randomSeed = scala.util.Random

  def initialization(numPopulation: Int, c: Constraints): Population = {
    (for{
      i <- 1 to numPopulation
    } yield new SeatDistribution(c)).toList
  }

  def orderPopulation(p: Population): Population = {
    p.sortBy(_.fitness).reverse
  }

  def selection(p: Population, S: Int): Population = {
    p.take(S)
  }

  def swapGenes(p: (Chromosome, Chromosome), crossPoint: Int):(Chromosome,Chromosome) = {
    val splitChromosome1 = p._1.splitAt(crossPoint)
    val splitChromosome2 = p._2.splitAt(crossPoint)
    (splitChromosome1._1:::splitChromosome2._2, splitChromosome2._1:::splitChromosome1._2)
  }

  def reproduceChromosomes(p: (Chromosome, Chromosome)):(Chromosome,Chromosome) = {
    val crossPoint = randomSeed.nextInt(p._1.length-1)+1
    swapGenes(p,crossPoint)
  }

  def matrixToList(m: Array[Array[Int]]): List[Int] ={
    m.flatten.toList
  }

  def listToMatrix(l: List[Int], c:Constraints): Array[Array[Int]] = {
    l.grouped(c.cols).map(_.toArray).toArray
  }

  def crossover(p: Population, numChildren: Int, c: Constraints): Population = {
    require(numChildren>1 && numChildren%2==0)
    val children = for{
      i <- 1 to numChildren/2
      randomParent1 = p(randomSeed.nextInt(p.length)).seats
      randomParent2 = p(randomSeed.nextInt(p.length)).seats
      children = reproduceChromosomes((matrixToList(randomParent1),matrixToList(randomParent2)))
    } yield List(new SeatDistribution(c, Some(listToMatrix(children._1,c))),
      new SeatDistribution(c, Some(listToMatrix(children._2,c))))
    children.toList.flatten
  }

  def mutation(distr: SeatDistribution): SeatDistribution = ???

  def runGeneticAlgorithm(N: Int, S: Int, numGenerations: Int, alphaMutation: Float,
                          c: Constraints): SeatDistribution = {
    //1 - Generate N=population random individuals
    var population = initialization(N, c)
    (1 to numGenerations) foreach { _ =>
      population = orderPopulation(population)
      //2 - Select S=selection BEST individuals
      val bestInidivuals = selection(population, S)
      //3 - Crossover
      val children = crossover(bestInidivuals, N-S, c)
      //4 - Mutation
      //5 - Substitution
      population = bestInidivuals ::: children
    }
    population.head
  }
}
