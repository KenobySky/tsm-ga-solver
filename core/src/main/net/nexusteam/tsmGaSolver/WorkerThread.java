package net.nexusteam.tsmGaSolver;

import com.badlogic.gdx.Gdx;

/**
 *
 * @author Andre Vinícius Lopes
 */
public class WorkerThread implements Runnable {

	private Controller controller;

	public volatile boolean stopToKillThread;

	public WorkerThread(Controller instance) {
		controller = instance;
	}

	@Override
	public void run() {
		if(!stopToKillThread) {
			float thisCost = 500.0f;
			float oldCost = 0.0f;
			int countSame = 0;

			controller.status = "Current cost: " + oldCost;
			controller.view.update();

			while(countSame < controller.minimum_non_change_generations)
				if(!stopToKillThread) {
					controller.generation_count++;
					controller.status = "Generation: " + controller.generation_count + " - Cost: " + thisCost + " - Mutated " + controller.genetic.getTimesMutated() + " Times";

					try{
					controller.genetic.iteration();
					}catch(Exception ex)
					{
						Gdx.app.error(getClass().getName(), "Genetic Iteration Critical Failure.Reason : "+ex.getMessage()+"\nAttempting to break loop and finish Thread...");
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

			controller.status = "Solution found after " + controller.generation_count + " generations and " + controller.genetic.getTimesMutated() + " mutations";
			controller.view.update();
			controller.setStarted(false);
		} else
			controller.status = "Halted thread! Thread currently stopped at generation " + controller.generation_count;
	}

}
