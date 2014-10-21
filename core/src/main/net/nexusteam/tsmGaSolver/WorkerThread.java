package net.nexusteam.tsmGaSolver;

import com.badlogic.gdx.Gdx;
import net.JeffHeatonCode.NeuralNetworkError;
import net.nexusteam.tsmGaSolver.tools.Benchmark;
import net.nexusteam.tsmGaSolver.views.Settings;

/**
 * @author Andre Vinicius Lopes
 */
public class WorkerThread extends Thread {

    protected Controller controller;

    /**
     * Master Flag To Stop The Thread.Prevents all Logic Inside be executed by
     * using an if Statement. May Cause Unpredictable Problems if its set True
     * During the while(countSame < controller.minimum_non_change_generations)
     * Loop.
     */
    private volatile boolean stopToKillThread;

    private Benchmark benchmark;

    /**
     *
     */
    private double iterationsCounter = 0;

    public WorkerThread(Controller instance) {
        controller = instance;
        if (Settings.prefs.getBoolean(Settings.BENCHMARK_THIS_RUN)) {
            benchmark = new Benchmark();
        }
    }

    @Override
    public void run() {
        if (!stopToKillThread) {
            float thisCost;
            float oldCost = 0;
            int non_change_geneations_counter = 0;

            controller.status = "Current cost: " + controller.getTopChromosome().getCost();
            controller.view.update();
            if (benchmark != null) {
                benchmark.start();
            }

            /**
             * If The non_change_geneations_counter is above the
             * minimum_non_change_generations , the thread should stop. If the
             * non_change_geneations_counter equals 0, then it shall run until
             * other restriction is satisfied to stop the algorithm.
             */
            while (non_change_geneations_counter < controller.minimum_non_change_generations || controller.minimum_non_change_generations == 0) {
               
                if (!stopToKillThread) {
                    controller.generation_count++;
                    controller.status = "Generation: " + controller.generation_count + " - Mutated " + controller.genetic.getMutationCounter() + " Times";

                    try {
                        controller.genetic.iteration();
                    } catch (NeuralNetworkError ex) {
                        Gdx.app.error(getClass().getName(), "Genetic Iteration Critical Failure.Reason : " + ex.getMessage() + "\nAttempting to break loop and finish Thread...");
                        stopToKillThread = true;
                        break;
                    }

                    thisCost = (float) controller.getTopChromosome().getCost();

                    if ((int) thisCost == (int) oldCost) {
                        non_change_geneations_counter++;
                    } else {
                        non_change_geneations_counter = 0;
                        oldCost = thisCost;
                    }

                    controller.view.update();
                    iterationsCounter++;

                    /**
                     * If The iterations counter is above the maximum
                     * generations, the thread should stop. If the
                     * controller.maximum_generations equals 0, then it shall
                     * run until other restriction is satisfied to stop
                     */
                    if (iterationsCounter >= controller.maximum_generations && controller.maximum_generations != 0) {
                        controller.status = "Solution found with " + controller.generation_count + " generations and " + controller.genetic.getMutationCounter() + " mutations";
                        controller.view.update();
                        controller.solutionFound();
                        break;
                    }
                } else {
                    Gdx.app.log(getClass().getSimpleName() + "\"" + getName() + "\"", "Thread stopped during a critical loop. Consequences may cause unpredictable results.\nAttempting to break loop and finish Thread...");
                    break;
                }
            }

            if (benchmark != null) {
                benchmark.end();
                benchmark.set(controller.generation_count, (float) controller.getTopChromosome().getCost(), controller.waypoints.length, controller.chromosome_quantity, controller.mutation_percentage, controller.mating_population_percentage, controller.favored_population_percentage, controller.cut_length, controller.minimum_non_change_generations, controller.genetic.getMutationCounter());
                benchmark.save(Settings.prefs.getString(Settings.CURRENT_SAMPLE), Settings.prefs.getString(Settings.NEW_BENCHMARK_NAME));
            }

            controller.status = "Solution found after " + controller.generation_count + " generations and " + controller.genetic.getMutationCounter() + " mutations";
            controller.view.update();
            controller.solutionFound();
        } else {
            controller.status = "Halted thread! Thread currently stopped at generation " + controller.generation_count;
        }

    }

    /**
     * Sets {@link #stopToKillThread} to true. Halts The Thread encapsulating
     * all logic in an if(!stopThread) Statement. All logic inside may lost
     * consistency. Controller Methods will attempt to interrupt and end this
     * thread if this method is called
     */
    public void stopToKillThread() {
        stopToKillThread = true;
    }

    /**
     * @return {@link #stopToKillThread}
     */
    public boolean isThreadStopping() {
        return stopToKillThread;
    }

}
