package main.net.nexusteam.tsmGaSolver;

import com.badlogic.gdx.math.MathUtils;
import main.net.nexusteam.tsmGaSolver.ann.TSPChromosome;
import main.net.nexusteam.tsmGaSolver.ann.TSPGeneticAlgorithm;
import main.net.nexusteam.tsmGaSolver.ann.Waypoint;
import main.net.nexusteam.tsmGaSolver.tools.Random;

/**
 *
 * @author Andre Vinícius Lopes
 */
public class Controller {

    //Reference to the View
    protected TsmGaSolverView view;

    /**
     * How many cities to use
     */
    protected int city_quantity = 3;

    /**
     * How many chromosomes to use
     */
    protected int chromosomes_quantity = 1000;

    /**
     * What percent of new-borns to mutate
     */
    protected float mutation_percentage = 0.100f;

    /**
     * The part of the population eligable for mateing.
     */
    protected int matingPopulationSize = chromosomes_quantity / 2;

    /**
     * The part of the population favored for mating.
     */
    protected int favoredPopulationSize = matingPopulationSize / 2;

    /**
     * How much genetic material to take during a mating.
     */
    protected int cutLength = city_quantity / 5;

    /**
     * The current generation,or epoc
     */
    protected int generation_count;

    /**
     * The background worker Thread
     */
    protected Thread worker;

    /**
     * Is the thread started.
     */
    protected boolean started = false;

    /**
     * Is the thread stopped.
     */
    protected boolean stop = false;

    protected TSPGeneticAlgorithm genetic;

    /**
     * Status String to draw in The view
     */
    public String status;

    /**
     * List of WayPoints
     */
    //waypoints = new ?
    
    
    /**
     *
     * @param viewInstance
     * @param city_quantity
     * @param chromossomes_quantity
     * @param mutation_percentage Percentage of the mutation of the Nodes
     * @param usable_Width Width of the usable Screen for The WayPoints Drawing
     * @param usable_Height Height of the usable Screen for The WayPoints
     * Drawing
     */
    public Controller(TsmGaSolverView viewInstance, int city_quantity, int chromossomes_quantity, float mutation_percentage, float usable_Width, float usable_Height) {
        this.view = viewInstance;
        this.city_quantity = city_quantity;
        this.chromosomes_quantity = chromossomes_quantity;
        this.mutation_percentage = mutation_percentage;
        initialize(usable_Width, usable_Height);
    }

    public Controller(TsmGaSolverView viewInstance, float usable_Width, float usable_Height) {
        this.view = viewInstance;
        this.city_quantity = 3;
        this.chromosomes_quantity = 1000;
        this.mutation_percentage = 0.100f;
        initialize(usable_Width, usable_Height);
    }

    /*
     Receives two parameters.
     They represent the usable area to generate the cities
     */
    private void initialize(float usable_Width, float usable_Height) {
        this.waypoints = new Waypoint[city_quantity];

        for (int i = 0; i < city_quantity; i++) {
            this.waypoints[i] = new Waypoint((MathUtils.random() * usable_Width), (MathUtils.random() * usable_Height), "" + Random.getRandomLetter());
        }

        genetic = new TSPGeneticAlgorithm(waypoints, chromosomes_quantity, mutation_percentage, 0.25, 0.5, city_quantity / 5);

    }

    private void start() {
        started = true;
        generation_count = 0;
        try {

            if (this.worker != null) {
                worker.interrupt();
                worker = null;
            }

        } catch (Exception ex) {
            worker = null;
            ex.printStackTrace();
        }

        worker = new Thread(new WorkerThread(this));
        worker.start();

    }

    public TSPChromosome getTopChromosome() {
        return genetic.getChromosome(0);
    }

}
