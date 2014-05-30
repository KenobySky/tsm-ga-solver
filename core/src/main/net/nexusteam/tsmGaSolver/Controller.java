package net.nexusteam.tsmGaSolver;

import net.nexusteam.tsmGaSolver.ann.TSPChromosome;
import net.nexusteam.tsmGaSolver.ann.TSPGeneticAlgorithm;
import net.nexusteam.tsmGaSolver.ann.Waypoint;
import net.nexusteam.tsmGaSolver.tools.RandomUtils;
import net.nexusteam.tsmGaSolver.views.Settings;
import net.nexusteam.tsmGaSolver.views.TsmGaSolver;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Andre Vinícius Lopes
 */
public class Controller {

	//Reference to the View
	protected TsmGaSolver view;

	/**
	 * How many chromosomes to use
	 */
	protected int chromosome_quantity = 1000;

	/**
	 * What percent of new-borns to mutate
	 */
	protected float mutation_percentage = 0.100f;

	/**
	 * The part of the population eligable for mateing.
	 */
	protected int matingPopulationSize = chromosome_quantity / 2;

	/**
	 * The part of the population favored for mating.
	 */
	protected int favoredPopulationSize = matingPopulationSize / 2;

	/**
	 * How much genetic material to take during a mating.
	 */
	protected int cutLength = 1;

	/**
	 * The current generation, or epoch
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

	public Controller(TsmGaSolver view, float usable_Width, float usable_Height) {
		this.view = view;
		configure();
		initialize(usable_Width, usable_Height);
	}

	/** configures this Controller based on the {@link #view} and {@link Settings#prefs preferences} */
	public void configure() {
		if(started)
			throw new IllegalStateException("configuring the Controller while it's running may produce unpredictable results");

		Preferences prefs = Settings.prefs;
		chromosome_quantity = prefs.getInteger(Settings.CHROMOSOME_QUANTITY);
		mutation_percentage = prefs.getFloat(Settings.MUTATION_PERCENTAGE);
		matingPopulationSize = prefs.getInteger(Settings.MATING_POPULATION_SIZE);
		favoredPopulationSize = prefs.getInteger(Settings.FAVORED_POPULATION_SIZE);
		cutLength = prefs.getInteger(Settings.CUT_LENGTH);
		// TODO Settings.MAXIMUM_GENERATIONS
		// TODO Settings.MAXIMUM_NON_CHANGE_GENERATIONS
	}

	/** Receives two parameters.
	 * 	They represent the usable area to generate the cities.<br>
	 * 	Should have been {@link #configure() configured} before. */
	public void initialize(float usable_Width, float usable_Height) {
		Array<Vector2> viewWaypoints = view.getWaypoints();
		waypoints = new Waypoint[viewWaypoints.size];

		for(int i = 0; i < waypoints.length; i++) {
			Vector2 point = viewWaypoints.get(i);
			waypoints[i] = new Waypoint(point.x, point.y, String.valueOf(RandomUtils.getRandomLetter()));
		}

		genetic = new TSPGeneticAlgorithm(waypoints, chromosome_quantity, mutation_percentage, 0.25, 0.5, waypoints.length / 5);
	}

	public void start() {
		stop();

		started = true;
		generation_count = 0;

		worker = new Thread(new WorkerThread(this));
		worker.start();

	}

	public void stop() {
		started = false;
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
	}

	//GETTERS AND SETTERS

	public TSPChromosome getTopChromosome() {
		return genetic.getChromosome(0);
	}

	public int getChromosomes_quantity() {
		return chromosome_quantity;
	}

	public void setChromosomes_quantity(int chromosomes_quantity) {
		chromosome_quantity = chromosomes_quantity;
	}

	public float getMutation_percentage() {
		return mutation_percentage;
	}

	public void setMutation_percentage(float mutation_percentage) {
		this.mutation_percentage = mutation_percentage;
	}

	public int getMatingPopulationSize() {
		return matingPopulationSize;
	}

	public void setMatingPopulationSize(int matingPopulationSize) {
		this.matingPopulationSize = matingPopulationSize;
	}

	public int getFavoredPopulationSize() {
		return favoredPopulationSize;
	}

	public void setFavoredPopulationSize(int favoredPopulationSize) {
		this.favoredPopulationSize = favoredPopulationSize;
	}

	public int getCutLength() {
		return cutLength;
	}

	public void setCutLength(int cutLength) {
		this.cutLength = cutLength;
	}

	public int getGeneration_count() {
		return generation_count;
	}

	public void setGeneration_count(int generation_count) {
		this.generation_count = generation_count;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Waypoint[] getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(Waypoint[] waypoints) {
		this.waypoints = waypoints;
	}

}
