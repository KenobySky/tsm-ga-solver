package net.nexusteam.tsmGaSolver.Ann;

import net.JeffHeatonCode.GeneticAlgorithm;
import net.JeffHeatonCode.NeuralNetworkError;

/**
 *
 * @author Andre Vinícius Lopes
 */
public class TSPGeneticAlgorithm extends GeneticAlgorithm<TSPChromosome>{

    public TSPGeneticAlgorithm(final Country cities[], final int populationSize,
			final double mutationPercent, final double percentToMate,
			final double matingPopulationPercent, final int cutLength)
			throws NeuralNetworkError {
		this.setMutationPercent(mutationPercent);
		this.setMatingPopulation(matingPopulationPercent);
		this.setPopulationSize(populationSize);
		this.setPercentToMate(percentToMate);
		this.setCutLength(cutLength);
		this.setPreventRepeat(true);

		setChromosomes(new TSPChromosome[getPopulationSize()]);
		for (int i = 0; i < getChromosomes().length; i++) {

			final TSPChromosome c = new TSPChromosome(this, cities);
			setChromosome(i, c);
		}
		sortChromosomes();
	}
    
}
