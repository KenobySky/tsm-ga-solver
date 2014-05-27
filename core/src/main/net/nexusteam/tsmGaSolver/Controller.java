package net.nexusteam.tsmGaSolver;

import net.nexusteam.tsmGaSolver.ann.TSPChromosome;
import net.nexusteam.tsmGaSolver.ann.TSPGeneticAlgorithm;
import net.nexusteam.tsmGaSolver.ann.Waypoint;
import net.nexusteam.tsmGaSolver.tools.Random;
import net.nexusteam.tsmGaSolver.views.TsmGaSolver;

import com.badlogic.gdx.math.MathUtils;

/**
 *
 * @author Andre Vinícius Lopes
 */
public class Controller {

	//Reference to the View
	protected TsmGaSolver view;

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
	Waypoint[] waypoints;

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
	public Controller(TsmGaSolver viewInstance, int city_quantity, int chromossomes_quantity, float mutation_percentage, float usable_Width, float usable_Height) {
		view = viewInstance;
		this.city_quantity = city_quantity;
		chromosomes_quantity = chromossomes_quantity;
		this.mutation_percentage = mutation_percentage;
		initialize(usable_Width, usable_Height);
	}

	public Controller(TsmGaSolver viewInstance, float usable_Width, float usable_Height) {
		view = viewInstance;
		city_quantity = 3;
		chromosomes_quantity = 1000;
		mutation_percentage = 0.100f;
		initialize(usable_Width, usable_Height);
	}

	/*
	 Receives two parameters.
	 They represent the usable area to generate the cities
	 */
	private void initialize(float usable_Width, float usable_Height) {
		waypoints = new Waypoint[city_quantity];

		for(int i = 0; i < city_quantity; i++)
			waypoints[i] = new Waypoint(MathUtils.random() * usable_Width, MathUtils.random() * usable_Height, "" + Random.getRandomLetter());

		genetic = new TSPGeneticAlgorithm(waypoints, chromosomes_quantity, mutation_percentage, 0.25, 0.5, city_quantity / 5);

	}

	
	private void start() {
		started = true;
		generation_count = 0;
		try {

			if(worker != null) {
				worker.interrupt();
				worker = null;
			}

		} catch(Exception ex) {
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
