
package net.nexusteam.tsmGaSolver.ann;

import com.badlogic.gdx.math.Vector2;
import net.JeffHeatonCode.GeneticAlgorithm;
import net.JeffHeatonCode.NeuralNetworkError;

/** @author Andre Vinícius Lopes */
public class TSPGeneticAlgorithm extends GeneticAlgorithm<TSPChromosome> {

	private int timesMutated = 0;

	public TSPGeneticAlgorithm (final Vector2 vector2s[], final int populationSize, final double mutationPercent,
		final double percentToMate, final double matingPopulationPercent, final int cutLength) throws NeuralNetworkError {

		setMutationPercent(mutationPercent);
		setMatingPopulation(matingPopulationPercent);
		setPopulationSize(populationSize);
		setPercentToMate(percentToMate);
		setCutLength(cutLength);
		setPreventRepeat(true);

		setChromosomes(new TSPChromosome[getPopulationSize()]);

		for (int i = 0; i < getChromosomes().length; i++) {
			final TSPChromosome c = new TSPChromosome(this, vector2s);
			setChromosome(i, c);
		}
		sortChromosomes();
	}

	public int getTimesMutated () {
		return timesMutated;
	}

	public void incrementMutationCounter () {
		timesMutated++;
	}

}
