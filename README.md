# Traveling salesman Solver with Genetic Algorithm and Libgdx #

*Authors: André V Lopes and Robin Stumm*

Using Mr Jeff Heaton Book, we used the code of the genetic algorithm chapter and changed the view to libGDX and made it more didatic for students.
Its possible to see the algorithm running, save samples, make benchmarks and even run it in android devices! 

To make the program the more didatic as possible, the following parameters of the algorithm can be changed :
1. Number of Waypoints
2. Number of Chromosomes
3. Mutation percentage
4. Mating population percentage
5. Favored population percentage
7. Maximum generations
8. Minimum non-change generations
9. Step Manually checkbox

The number of waypoints is the quantity of cities that is inserted in the map.They are represented by white dots.And they are connected by green lines during the process.

![Nova Imagem de Bitmap.jpg](https://bitbucket.org/repo/egL9o4/images/1237400943-Nova%20Imagem%20de%20Bitmap.jpg)

Below the map, there is two labels which shows the current status and the current optimum cost.  
On the right, there are the following buttons:

* Start
* Samples
* Settings
* Benchmark

Its  possible to save Samples to be used in benchmarks.

![Nova Imagem de Bitmap.jpg](https://bitbucket.org/repo/egL9o4/images/1790321101-Nova%20Imagem%20de%20Bitmap.jpg)

With benchmarks it's possible to make the program to save the result of the solution calculated in a file. It also saves the sample reference which makes it possible to run the same sample of cities with different settings and compare the results, making it very didatic for students.  
The benchmark is saved when a solution is calculated. It will save the following data:

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

![Nova Imagem de Bitmap.jpg](https://bitbucket.org/repo/egL9o4/images/2563788321-Nova%20Imagem%20de%20Bitmap.jpg)

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

###  SPECIAL THANKS  ###

JEFF HEATON

http://www.heatonresearch.com/

HEATON, JEFF. Introduction to Neural Networks with Java, Second Edition. 2 edition (October 1, 2008). Publish Location: Heaton Research, Inc, October 1, 2008. 440 pages.