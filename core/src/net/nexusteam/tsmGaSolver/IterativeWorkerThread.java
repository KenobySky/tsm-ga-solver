package net.nexusteam.tsmGaSolver;

import com.badlogic.gdx.Gdx;

/**
 * @author André Vinícius Lopes
 */
public class IterativeWorkerThread extends WorkerThread {

    /**
     * Number Of Iterations per Step/Button Click in View
     */
    private volatile int numberOfIterations = 1;

    /**
     * Flag to indicate that the Thread is waiting for the user to click the
     * STEP button to make 1 or {@link #numberOfIterations} iterations
     */
    private volatile boolean awaitsUserInput;

    public IterativeWorkerThread(Controller instance, int numberOfIterations) {
        super(instance);
        this.numberOfIterations = numberOfIterations;
    }

    @Override
    public void run() {
        if (!isThreadStopping()) {
            if (numberOfIterations > 0) {
                awaitsUserInput = false;
                float thisCost = 500.0f;
                float oldCost = 0.0f;
                int countSame = 0;

                controller.status = "Current cost: " + oldCost;
                controller.view.update();

                while (countSame < controller.minimum_non_change_generations) {
                    if (!isThreadStopping() && numberOfIterations > 0) {
                        controller.generation_count++;
                        controller.status = "Generation: " + controller.generation_count + " - Mutated " + controller.genetic.getMutationCounter() + " Times";

                        controller.genetic.iteration();

                        thisCost = (float) controller.getTopChromosome().getCost();

                        if ((int) thisCost == (int) oldCost) {
                            countSame++;
                        } else {
                            countSame = 0;
                            oldCost = thisCost;
                        }

                        controller.view.update();
                        numberOfIterations--;
                    } else {
                        Gdx.app.log(getClass().getSimpleName() + "\"" + getName() + "\"", "Thread stopped during a critical loop. Consequences may cause unpredictable results.\nAttempting to break loop and finish thread.");
                        break;
                    }
                }

                controller.status = "Solution found after " + controller.generation_count + " generations and " + controller.genetic.getMutationCounter() + " mutations";
                controller.view.update();
                if (controller.isRunning()) {
                    controller.stop();
                }
                numberOfIterations--;
            }
            controller.status = "Halted thread! Thread currently stopped at generation " + controller.generation_count;
            Gdx.app.log(getClass().getSimpleName() + "\"" + getName() + "\"", "All iterations complete. Click \"step\" to restart.");
            awaitsUserInput = true;
            controller.solutionFound();
        } else {
            System.out.println("Thread stopped! Consequences may be unpredictable results.");
        }
        controller.status = "Halted thread! Thread currently stopped at generation " + controller.generation_count;
    }

    /**
     * Changes the Number of Iterations
     *
     * @param iterations
     */
    public void changeNumberOfIterations(int iterations) {
        if (numberOfIterations > 0) {
            Gdx.app.debug(getClass().getName(), "Denied: cannot change number of iterations during a step-flow");
        } else {
            numberOfIterations = iterations;
            Gdx.app.log(getClass().getName(), "Accepted: Number of iterations changed to " + numberOfIterations);
        }
    }

    public boolean awaitsUserInput() {
        return awaitsUserInput;
    }

}
