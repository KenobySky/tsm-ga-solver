package net.nexusteam.tsmGaSolver;

import java.text.NumberFormat;

/**
 * 
 * @author Andre Vinícius Lopes
 */
public class WorkerThread implements Runnable {

	private Controller controller;
	public boolean stopThread = false;

	public WorkerThread(Controller instance) {
		controller = instance;
	}

	@Override
	public void run() {
		if (!stopThread) {
			float thisCost = 500.0f;
			float oldCost = 0.0f;
			int countSame = 0;

			controller.status = "Current cost: " + oldCost;
			controller.view.update();

			final NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(2);
			nf.setMinimumFractionDigits(2);

			// controller.minimum_non_change_generations
			while (countSame < controller.minimum_non_change_generations) {
				controller.generation_count++;
				controller.status = "Generation: "
						+ controller.generation_count + " - Cost: " + thisCost
						+ " - Mutated " + nf.format(0) + "%";

				controller.genetic.iteration();

				thisCost = (float) controller.getTopChromosome().getCost();

				if ((int) thisCost == (int) oldCost)
					countSame++;
				else {
					countSame = 0;
					oldCost = thisCost;
				}

				controller.view.update();
			}

			controller.status = "Solution found after "
					+ controller.generation_count + " generations!";
			controller.view.update();
			controller.setStarted(false);
		}else{
			controller.status = "Halted Thread! Thread Currently Stopped! At : "+ controller.generation_count + " Generation";
			return;
		}
	}

}
