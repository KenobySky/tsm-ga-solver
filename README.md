# Traveling salesman Solver with Genetic Algorithm and Libgdx #

*Authors: André V Lopes and Robin Stumm*

We used the code of the genetic algorithm chapter in Mr Jeff Heaton's book and added a libGDX UI to finally make it more didactic for students.
You can see the algorithm running, save samples and take benchmarks.

This project runs on desktop devices with Java installed and Android 2.2+ with OpenGL 2.0+ capabilities.

To make the program as didactic as possible, picture 1 shows the following parameters of the algorithm that can be changed:

1. Number of waypoints/cities
2. Number of chromosomes
3. Mutation percentage
4. Mating population percentage
5. Favored population percentage
6. Maximum generations
7. Minimum non-change generations
8. Step manually checkbox

The number of waypoints is the quantity of cities that is inserted into the map. They are represented by white dots and connected by green lines during the process.

![1786806396-Settings.png](https://bitbucket.org/repo/egL9o4/images/2199362518-1786806396-Settings.png)

*Picture 1: Settings Window*

Picture 2 shows the main screen before the solution was calculated and picture 3 shows the solution displayed on screen.

![2266976735-mainscreen.png](https://bitbucket.org/repo/egL9o4/images/2125412026-2266976735-mainscreen.png)

*Picture 2: Main screen*

![98117339-solutionCalculated.png](https://bitbucket.org/repo/egL9o4/images/2027184326-98117339-solutionCalculated.png)

*Picture 3: Solution Calculated*

Below the map there are two labels displaying the current status and optimum cost. On the right there are the following buttons:

* Start
* Samples
* Settings
* Benchmark

Once you saved a sample you can take benchmarks of it (see picture 4):

![784360299-benchmakToSave.png](https://bitbucket.org/repo/egL9o4/images/2266868724-784360299-benchmakToSave.png)

*Picture 4: Benchmark*

Benchmarks save the current settings to a file next to the sample file. This is useful to, for example, run the same sample of cities with different settings and compare the results, making it easier for students to understand the effects of different settings.
The benchmark is saved when a solution is calculated. It will save the following data as you can seen in picture 5:

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

There are precompiled jar files available for [download](https://bitbucket.org/andrelopes1705/tsm-ga-solver/downloads). Students and contributors can however build the application very easily.  
We use [gradle](http://gradle.org) for builds and dependency management. Assuming you have [Mercurial](http://mercurial.selenic.com) installed, open a shell and run the following commands to download the source, build an executable jar and run it:

```
#!bash
hg clone http://bitbucket.org/andrelopes1705/tsm-ga-solver TSM-GA-Solver
cd TSM-GA-Solver
gradlew.bat :desktop:run
```

The executable jar can be found in `desktop/build/libs/`. You can rebuild it any time without running the application using `gradlew.bat :desktop:build`.

If you do not want to install Mercurial you can also [download](https://bitbucket.org/andrelopes1705/tsm-ga-solver/downloads) the repository normally.

### Contribution guidelines ###

* Writing tests
* Code review
* Other guidelines

### Who do I talk to? ###

Send a private message to [André Lopes](http://bitbucket.org/andrelopes1705) or [Robin Stumm](http://bitbucket.org/dermetfan).

#### SPECIAL THANKS  ####

JEFF HEATON

http://www.heatonresearch.com/

HEATON, JEFF. Introduction to Neural Networks with Java, Second Edition. 2 edition (October 1, 2008). Publish Location: Heaton Research, Inc, October 1, 2008. 440 pages.