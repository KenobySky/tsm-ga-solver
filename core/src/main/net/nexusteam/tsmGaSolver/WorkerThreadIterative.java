package net.nexusteam.tsmGaSolver;

import java.text.NumberFormat;

/**
 *
 * @author Andre Vinícius Lopes
 */
public class WorkerThreadIterative implements Runnable {

	private Controller controller;

	/**
	 * Master Flag To Stop The Thread.Prevents all Logic Inside be executed by using an if Statement.
	 * May Cause Unpredictable Problems if its set True During the
	 * while(countSame < controller.minimum_non_change_generations) Loop.
	 */
	private boolean stopToKillThread = false;

	/**
	 * Number Of Iterations per Step/Button Click in View
	 */
	private int numberOfIterations = 1;

	/**
	 * Flag to indicate that the Thread is waiting for the user to click the STEP button to make 1 or (*numberOfIterations*Variable) iterations
	 */
	private boolean waitingUserInput = false;

	public WorkerThreadIterative(Controller instance, int numberOfIterations) {
		controller = instance;
		this.numberOfIterations = numberOfIterations;
	}

	@Override
	public void run() {
		if(!stopToKillThread) {

			if(numberOfIterations > 0)
			{
				waitingUserInput = false;
				float thisCost = 500.0f;
				float oldCost = 0.0f;
				int countSame = 0;

				controller.status = "Current cost: " + oldCost;
				controller.view.update();

				while(countSame < controller.minimum_non_change_generations) {
					if(!stopToKillThread && numberOfIterations > 0)
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
						numberOfIterations--;
					} else
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
				numberOfIterations--;

			} else
			{
				controller.status = "Halted thread! Thread currently stopped at generation " + controller.generation_count;
				System.out.println("Ended Number Of Iterations" + " " + "Waiting Another Click on Step Button To Continue");
				waitingUserInput = true;
			} // END OF *IF* NUMBER OF ITERATIONS 

		} else
		controller.status = "Halted thread! Thread currently stopped at generation " + controller.generation_count;
		return;

	}// END OF *IF* STOP THREAD

	/**
	 * returns true if the number of iterations <= 0 
	 */
	public boolean isWaitingUser()
	{
		return waitingUserInput;
	}

	/**
	 * Changes the Number of Iterations
	 */
	public void changeNumberOfIterations(int iterations)
	{
		if(numberOfIterations > 0)
		{
			System.out.println("Denied : cannot change number of iterations during a step-flow");

		} else
		{
			numberOfIterations = iterations;
			System.out.println("Accepted : Number Of Iterations changed to : " + numberOfIterations);
		}
	}

	/**
	 * Set stopThread = true;
	 * Halts The Thread encapsulating all logic in an if(!stopThread) Statement, even if numberOfIterations > 0
	 * All logic inside may lost consistency.
	 * Controller Methods will attempt to interrupt and end this thread if this method is called
	 */
	public void stopToKillThread()
	{
		stopToKillThread = true;
	}

	

	/**
	 * Returns stopThread boolean
	 */
	public boolean isThreadStopped()
	{
		return stopToKillThread;
	}

}
