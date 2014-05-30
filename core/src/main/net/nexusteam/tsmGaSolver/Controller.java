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
	protected float mating_population_percentage = .5f;

	/**
	 * The part of the population favored for mating.
	 */
	protected float favored_population_percentage = .5f;

	/**
	 * How much genetic material to take during a mating.
	 */
	protected int cut_length = 1;

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
		mating_population_percentage = prefs.getFloat(Settings.MATING_POPULATION_PERCENTAGE);
		favored_population_percentage = prefs.getFloat(Settings.FAVORED_POPULATION_PERCENTAGE);
		cut_length = prefs.getInteger(Settings.CUT_LENGTH);
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

		genetic = new TSPGeneticAlgorithm(waypoints, chromosome_quantity, mutation_percentage, mating_population_percentage, favored_population_percentage, cut_length);
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

}
