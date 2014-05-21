package net.nexusteam.tsmGaSolver.ann;

import net.JeffHeatonCode.GeneticAlgorithm;
import net.JeffHeatonCode.NeuralNetworkError;

/**
 *
 * @author Andre Vinícius Lopes
 */
public class TSPGeneticAlgorithm extends GeneticAlgorithm<TSPChromosome> {

	public TSPGeneticAlgorithm(final Waypoint cities[], final int populationSize, final double mutationPercent, final double percentToMate, final double matingPopulationPercent, final int cutLength) throws NeuralNetworkError {
		setMutationPercent(mutationPercent);
		setMatingPopulation(matingPopulationPercent);
		setPopulationSize(populationSize);
		setPercentToMate(percentToMate);
		setCutLength(cutLength);
		setPreventRepeat(true);

		setChromosomes(new TSPChromosome[getPopulationSize()]);
		for(int i = 0; i < getChromosomes().length; i++) {
			final TSPChromosome c = new TSPChromosome(this, cities);
			setChromosome(i, c);
		}
		sortChromosomes();
	}

}
