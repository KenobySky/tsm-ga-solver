package net.nexusteam.tsmGaSolver;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import net.nexusteam.tsmGaSolver.tools.Benchmark;
import net.nexusteam.tsmGaSolver.tools.Sample;

/** @author Andre Vinicius Lopes */
public class WorkerThread implements Runnable {

	private Controller controller;

	public volatile boolean stopToKillThread;

	private Benchmark benchmark;

	public WorkerThread(Controller instance) {
		controller = instance;
		Sample sample = new Sample(controller.view.getWaypoints());
		String sampleName = String.valueOf(TimeUtils.millis()); // TODO let user decide sample name
		sample.save(sampleName);// TODO let user decide if a sample should be saved
		benchmark = new Benchmark(sampleName);// TODO let user decide if a benchmark should be made
	}

	@Override
	public void run() {
		if(!stopToKillThread) {
			float thisCost;
			float oldCost = 0;
			int countSame = 0;

			controller.status = "Current cost: " + controller.getTopChromosome().getCost();
			controller.view.update();

			while(countSame < controller.minimum_non_change_generations) {
				if(!stopToKillThread) {
					controller.generation_count++;
					controller.status = "Generation: " + controller.generation_count + " - Cost: " + controller.getTopChromosome().getCost() + " - Mutated " + controller.genetic.getTimesMutated() + " Times";

					try {
						controller.genetic.iteration();
					} catch(Exception ex) {
						Gdx.app.error(getClass().getName(), "Genetic Iteration Critical Failure.Reason : " + ex.getMessage() + "\nAttempting to break loop and finish Thread...");
						stopToKillThread = true;
						break;
					}

					thisCost = (float) controller.getTopChromosome().getCost();

					if((int) thisCost == (int) oldCost)
						countSame++;
					else {
						countSame = 0;
						oldCost = thisCost;
					}

					controller.view.update();
				} else {
					Gdx.app.error(getClass().getName(), "Thread stopped during a critical loop. Consequences may cause unpredictable results.\nAttempting to break loop and finish Thread...");
					break;
				}
			}

			benchmark.set(controller.generation_count, (float) controller.getTopChromosome().getCost(), controller.waypoints.length, controller.chromosome_quantity, controller.mutation_percentage, controller.mating_population_percentage, controller.favored_population_percentage, controller.cut_length, controller.minimum_non_change_generations, controller.genetic.getTimesMutated());
			benchmark.save(false);

			controller.status = "Solution found after " + controller.generation_count + " generations and " + controller.genetic.getTimesMutated() + " mutations";
			controller.view.update();
			controller.setStarted(false);
		} else
			controller.status = "Halted thread! Thread currently stopped at generation " + controller.generation_count;
	}

}
