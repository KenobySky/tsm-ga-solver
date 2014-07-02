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

/** @author Andre Vinícius Lopes */
public class Controller {

	protected TsmGaSolver view;

	/** How many chromosomes to use */
	protected int chromosome_quantity = 1000;

	/** What percent of new-borns to mutate */
	protected float mutation_percentage = 0.100f;

	/** The part of the population eligible for mating. */
	protected float mating_population_percentage = .5f;

	/** The part of the population favored for mating. */
	protected float favored_population_percentage = .5f;

	/** How much genetic material to take during a mating. */
	protected int cut_length;

	protected int minimum_non_change_generations;

	/** The current generation, or epoch */
	protected int generation_count;

	/** The Reference to the Currently background worker Thread */
	private WorkerThread workerThread;

	protected TSPGeneticAlgorithm genetic;

	/** Status String to draw in The view */
	public String status;

	/** List of WayPoints */
	Waypoint[] waypoints;

	/** an optional solutionFound */
	private Runnable callback;

	public Controller(TsmGaSolver view, float usable_Width, float usable_Height) {
		this.view = view;
		configure();
		initialize(usable_Width, usable_Height);
	}

	public Controller(TsmGaSolver view, float usable_width, float usable_height, Runnable callback) {
		this(view, usable_width, usable_height);
		this.callback = callback;
	}

	/** configures this Controller based on the {@link #view} and {@link Settings#prefs preferences} */
	public void configure() {
		if(isRunning())
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

	/** Receives two parameters. They represent the usable area to generate the cities.<br> */
	public void initialize(float usable_Width, float usable_Height) {
		configure();
		Array<Vector2> viewWaypoints = view.getWaypoints();
		waypoints = new Waypoint[viewWaypoints.size];

		for(int i = 0; i < waypoints.length; i++) {
			Vector2 point = viewWaypoints.get(i);
			waypoints[i] = new Waypoint(point.x, point.y, String.valueOf(RandomUtils.getRandomLetter()));
		}

		cut_length = chromosome_quantity / 5; // TODO remove from Settings if this is not configurable (currently apparently needs to be chromosome_quantity / 5)

		genetic = new TSPGeneticAlgorithm(waypoints, chromosome_quantity, mutation_percentage, mating_population_percentage, favored_population_percentage, cut_length);
	}

	/** starts the normal mode */
	public void start() {
		start(-1);
	}

	/** @param numberOfIterations The number of iterations for iterative mode. Set to 0 or smaller for normal mode. */
	public void start(int numberOfIterations) {
		if(isRunning())
			throw new IllegalStateException("Can't start: Algorithm is already running");

		generation_count = 0;

		workerThread = numberOfIterations <= 0 ? new WorkerThread(this) : new IterativeWorkerThread(this, numberOfIterations);
		workerThread.start();
	}

	/** Stops The Full Mode */
	public void stop() {
		if(!isRunning())
			throw new IllegalStateException("Can't stop: Algorithm is not running");
		try {
			if(workerThread != null && !workerThread.isThreadStopping()) {
				workerThread.stopToKillThread();
				workerThread.interrupt();
				workerThread = null;
			}
		} catch(Exception ex) {
			Gdx.app.error(getClass().getName(), "WorkerThread \"" + workerThread.getName() + "\" could not be stopped", ex);
		}
	}

	/** Runs the {@link #callback} if any. */
	public void solutionFound() {
		if(callback != null)
			callback.run();
	}

	public TSPChromosome getTopChromosome() {
		return genetic.getChromosome(0);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////
	// ITERATIVE METHODS BELOW
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////

	/** Step The WorkerThreadIterative Mode */
	public void step(int numberOfIterations) {
		if(isRunning())
			stop();

		IterativeWorkerThread iterWorkerThread = workerThread instanceof IterativeWorkerThread ? (IterativeWorkerThread) workerThread : null;

		if(iterWorkerThread == null)
			iterWorkerThread = new IterativeWorkerThread(this, numberOfIterations);
		else if(iterWorkerThread.awaitsUserInput())
			iterWorkerThread.changeNumberOfIterations(numberOfIterations);

		workerThread = iterWorkerThread;
	}

	// GETTERS AND SETTERS

	public boolean isRunning() {
		return workerThread != null && !workerThread.isThreadStopping() && !workerThread.isInterrupted();
	}

	public Runnable getCallback() {
		return callback;
	}

	public void setCallback(Runnable callback) {
		this.callback = callback;
	}

}
