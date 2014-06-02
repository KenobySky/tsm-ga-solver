package net.nexusteam.tsmGaSolver;

import java.text.NumberFormat;

/**
 *
 * @author Andre Vinícius Lopes
 */
public class WorkerThread implements Runnable {

	private Controller controller;
	
	public boolean stopToKillThread = false;

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

			//final NumberFormat nf = NumberFormat.getInstance();
			//nf.setMinimumFractionDigits(2);
			//nf.setMinimumFractionDigits(2);

			// controller.minimum_non_change_generations
			while(countSame < controller.minimum_non_change_generations) {
				if(!stopToKillThread)
				{
					controller.generation_count++;
					controller.status = "Generation: " + controller.generation_count + " - Cost: " + thisCost + " - Mutated " + controller.genetic.getTimesMutated() + " Times";

					controller.genetic.iteration();

					thisCost = (float) controller.getTopChromosome().getCost();

					if((int) thisCost == (int) oldCost)
						countSame++;
					else {
						countSame = 0;
						oldCost = thisCost;
					}

					controller.view.update();
				}else
				{
					System.out.println("Thread Stopped During a critical loop.Consequences May cause unpredictable Results");
					System.out.println("Attempt To Break Loop and finish Thread.");
					System.out.println("Breaking Loop...");
					break;
				}
			}

			controller.status = "Solution found after " + controller.generation_count + " generations.And after " + controller.genetic.getTimesMutated() + " Mutations";
			controller.view.update();
			Controller.setStarted(false);

		} else
			controller.status = "Halted thread! Thread currently stopped at generation " + controller.generation_count;
	}

}
