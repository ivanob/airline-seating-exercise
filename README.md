
Ivan Obeso Aguera (ivan.obeso1@gmail.com). 07/06/17
The description of the problem can be found in "Problem description.pdf"

Explanation of the approach:
============================

The problem falls into the category of "optimization problems". It is a
combinational NP-hard problem and I did not find an obvious algorithm to
solve it with the optimal solution. I could come up with some heuristics
like this:
"trying to place the biggest group available in a row, and then
fill the rest of the row with the biggest available group again..."
but this greedy solution is not optimal, for example when we have this
configuration:

3 1
(1W,2W,3W)
(4)
(5)
(6)

The optimal solution is [4,5,6]

...So, as it was a requirement from the exercise to maximize the happiness
of the passengers, and given a distribution we can calculate that score,
I think this is a good candidate to use genetic algorithms.

I have implemented a small genetic algorithm in Scala. There are 3 main classes
in the project:
- Contraints just holds the parameters of the problem: Size of the plane,
groups of passengers...). I created some extra data structures here
to make the implementation easier.

- SeatDistribution represents a solution of the problem. It generates a random
distribution of seats given the constraints we are following. The most important
function here is the one who verifies if a group is happily seated, as this is
the base of the fitness algorithm.

- GeneticAlgorithm: It is a standard genetic algorithm. It is interesting to
explain how the crossover is done: The way to combine 2 good solutions is
to randomly choose a point on the matrix and swap both to generate 2 new
solutions. This can produce non-valid distributions. For example:

We have only 1 row and 4 seats. Out distributions are: [1,2,3,4] and [4,3,2,1].
When we split them in a half and combine, we generate: [1,2,2,1] and [4,3,3,4].
It is necessary to fix this solutions. The way I did is just replacing the
repeated passenger by passengers who are being left out (or empty seats).

The main function of the project is:
runGeneticAlgorithm(N: Int, S: Int, numGenerations: Int, alphaMutation: Float, c: Constraints)
...Where N is the size of the initial population, S the size of the population
of each generation, numGenerations is the number of generations and alphaMutation
is the probability of having a mutation (I did not implemented this)

All the functions are covered by unit tests.

How to run it:
==============
It is an SBT project created with intellij. You can import it in your IDE and
run the Main object or you can run just "sbt run" in your command line inside
the project. Please, take into account depending on the parameters it can
take a while to finish the simulation.
