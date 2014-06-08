package net.nexusteam.tsmGaSolver;

import net.nexusteam.tsmGaSolver.ann.TSPChromosome;
import net.nexusteam.tsmGaSolver.ann.TSPGeneticAlgorithm;
import net.nexusteam.tsmGaSolver.ann.Waypoint;
import net.nexusteam.tsmGaSolver.tools.RandomUtils;
import net.nexusteam.tsmGaSolver.views.Settings;
import net.nexusteam.tsmGaSolver.views.TsmGaSolver;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Andre Vinícius Lopes
 */
public class Controller {

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
	 * The part of the population eligible for mating.
	 */
	protected float mating_population_percentage = .5f;

	/**
	 * The part of the population favored for mating.
	 */
	protected float favored_population_percentage = .5f;

	/**
	 * How much genetic material to take during a mating.
	 */
	protected int cut_length = 1;

	protected int minimum_non_change_generations;

	/**
	 * The current generation, or epoch
	 */
	protected int generation_count;

	/**
	 * The Encapsulation of background worker Thread
	 */
	protected Thread worker;

	/**
	 * The Reference to the Currently background worker Thread
	 */
	private WorkerThread workerThread;

	/**
	 * The Reference to the Currently background ITERATIVE worker Thread
	 */
	private WorkerThreadIterative iterativeWorkerThread;

	/**
	 * Is the thread started.
	 */
	private boolean started;

	/**
	 * Is the thread stopped.
	 */
	protected boolean stop;

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
		if(started && workerThread != null && worker != null && worker.isInterrupted())
			throw new IllegalStateException("Denied: Configuring the Controller while it's running may produce unpredictable results");

		Preferences prefs = Settings.prefs;
		chromosome_quantity = prefs.getInteger(Settings.CHROMOSOME_QUANTITY);
		mutation_percentage = prefs.getFloat(Settings.MUTATION_PERCENTAGE);
		mating_population_percentage = prefs.getFloat(Settings.MATING_POPULATION_PERCENTAGE);
		favored_population_percentage = prefs.getFloat(Settings.FAVORED_POPULATION_PERCENTAGE);
		cut_length = prefs.getInteger(Settings.CUT_LENGTH);
		// TODO Settings.MAXIMUM_GENERATIONS
		minimum_non_change_generations = prefs.getInteger(Settings.MINIMUM_NON_CHANGE_GENERATIONS);
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

		// TODO remove from Settings if this is not configurable (currently apparently needs to be chromosome_quantity / 5)
		cut_length = chromosome_quantity / 5;
		//System.out.println("Cut : " + cut_length);
		//System.out.println("Chromo q : " + chromosome_quantity);

		genetic = new TSPGeneticAlgorithm(waypoints, chromosome_quantity, mutation_percentage, mating_population_percentage, favored_population_percentage, cut_length);
	}

	/**
	 * Starts the Full Mode
	 */
	public void start() {
		stop();

		started = true;
		generation_count = 0;

		workerThread = new WorkerThread(this);
		worker = new Thread(workerThread);
		worker.start();

	}

	/**
	 * Stops The Full Mode
	 */
	public void stop() {
		try {
			if(workerThread != null)
				workerThread.stopToKillThread = true;

			if(worker != null) {
				worker.interrupt();
				worker = null;
			}
		} catch(Exception ex) {
			Gdx.app.error(getClass().getName(), worker.getName() + " could not be stopped", ex);
		}
	}

	public TSPChromosome getTopChromosome() {
		return genetic.getChromosome(0);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	//ITERATIVE METHODS BELOW
	/////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Step The WorkerThreadIterative Mode
	 */
	public void step(int numberOfIterations) {
		//Check if Full Mode is Running
		boolean isFullModeRunning = isWorkerThreadRunning();

		if(isFullModeRunning)
			stop();

		if(iterativeWorkerThread == null)
			iterativeWorkerThread = new WorkerThreadIterative(this, numberOfIterations);
		else if(iterativeWorkerThread.isWaitingUser())
			iterativeWorkerThread.changeNumberOfIterations(numberOfIterations);
		else if(iterativeWorkerThread.isWaitingUser() && iterativeWorkerThread.isThreadStopped())
			Gdx.app.error(getClass().getName(), "Iterative worker thread is waiting another user input but thread is killed!");
	}

	/**
	 * Kill The IterativeWorkerThread Mode
	 */
	public void killWorkerThreadIterative() {
		if(iterativeWorkerThread != null) {
			if(!iterativeWorkerThread.isThreadStopped()) {
				iterativeWorkerThread.stopToKillThread();
				if(worker != null)
					worker.interrupt();
			}
		} else if(iterativeWorkerThread == null)
			if(worker != null)
				worker.interrupt();

		System.out.println("IterativeWorkerThread Destroyed.");
	}

	private boolean isWorkerThreadRunning() {
		if(workerThread != null && worker != null && !workerThread.stopToKillThread)
			return !worker.isInterrupted();
		return false;
	}

	// GETTERS AND SETTERS

	/**
	 * Is the thread started.
	 */
	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

}
