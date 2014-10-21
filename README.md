# Traveling salesman Solver with Genetic Algorithm and Libgdx #

*Authors: André V Lopes and Robin Stumm*

Using Mr Jeff Heaton Book, we used the code of the genetic algorithm chapter and changed the view to libGDX and made it more didatic for students.
Its possible to see the algorithm running, save samples, make benchmarks and even run it in android devices! 

To make the program as didatic as possible, picture 1 shows the following parameters of the algorithm that can be changed:

1. Number of Waypoints/Cities
2. Number of Chromosomes
3. Mutation percentage
4. Mating population percentage
5. Favored population percentage
7. Maximum generations
8. Minimum non-change generations
9. Step Manually checkbox

![1786806396-Settings.png](https://bitbucket.org/repo/egL9o4/images/2199362518-1786806396-Settings.png)

*Picture 1: Settings Window*

The number of waypoints is the quantity of cities that is inserted in the map.They are represented by white dots.And they are connected by green lines during the process.  
Picture 2 shows the main screen before the solution was calculated and pictue 3 shows the solution displayed on screen.

![2266976735-mainscreen.png](https://bitbucket.org/repo/egL9o4/images/2125412026-2266976735-mainscreen.png)

*Picture 2: Main screen*

![98117339-solutionCalculated.png](https://bitbucket.org/repo/egL9o4/images/2027184326-98117339-solutionCalculated.png)

*Picture 3: Solution Calculated*

Below the map, there is two labels which shows the current status and the current optimum cost.
In the right, there is the following buttons :

* Start
* Samples
* Settings
* Benchmark

Once you saved a sample, you can take benchmarks of it (as shown in picture 4):

![784360299-benchmakToSave.png](https://bitbucket.org/repo/egL9o4/images/2266868724-784360299-benchmakToSave.png)

*Picture 4: Benchmark*

With benchmarks it's possible to make the program to save the result of the solution calculated in a file. It also saves the sample reference which makes it possible to run the same sample of cities with different settings and compare the results, making it very didatic for students.  
The benchmark is saved when a solution is calculated. It will save the following data, as can be seen in picture 5:

* Date and time of start
* Duration in seconds
* Waypoint quantity
* Chromosome quantity
* Cost in pixels
* Cut length
* Iterations
* Mating population percentage
* Favored population percentage
* Mutation percentage
* Mutation quantity
* Minimum non-change generations

![2660592877-BenchmarkSaved.png](https://bitbucket.org/repo/egL9o4/images/3671443975-2660592877-BenchmarkSaved.png)

*Picture5 : Benchmark saved*

### How do I get set up? ###

* Summary of set up
* Configuration
* Dependencies
* Database configuration
* How to run tests
* Deployment instructions

### Contribution guidelines ###

* Writing tests
* Code review
* Other guidelines

### Who do I talk to? ###

Private message [André Lopes](http://bitbucket.org/andrelopes1705) or [Robin Stumm](http://bitbucket.org/dermetfan).

#### SPECIAL THANKS  ####

JEFF HEATON

http://www.heatonresearch.com/

HEATON, JEFF. Introduction to Neural Networks with Java, Second Edition. 2 edition (October 1, 2008). Publish Location: Heaton Research, Inc, October 1, 2008. 440 pages.