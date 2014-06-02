package net.nexusteam.tsmGaSolver.ann;

import net.JeffHeatonCode.Chromosome;
import net.JeffHeatonCode.NeuralNetworkError;

/**
 * 
 * @author ?
 */
public class TSPChromosome extends Chromosome<Integer, TSPGeneticAlgorithm> {

	protected Waypoint[] path;
	

	public TSPChromosome(final TSPGeneticAlgorithm owner, final Waypoint path[]) {
		setGeneticAlgorithm(owner);
		this.path = path;

		final Integer genes[] = new Integer[this.path.length];
		final boolean taken[] = new boolean[path.length];

		for (int i = 0; i < genes.length; i++) {
			taken[i] = false;
		}
		for (int i = 0; i < genes.length - 1; i++) {
			int icandidate;

			do {
				icandidate = (int) (Math.random() * genes.length);
			} while (taken[icandidate]);

			genes[i] = icandidate;
			taken[icandidate] = true;
			if (i == genes.length - 2) {
				icandidate = 0;

				while (taken[icandidate]) {
					icandidate++;
				}

				genes[i + 1] = icandidate;
			}
		}
		setGenes(genes);
		calculateCost();

	}

	@Override
	public void calculateCost() throws NeuralNetworkError {
		double cost = 0.0;
		for (int i = 0; i < path.length - 1; i++) {
			final double dist = path[getGene(i)].dst(path[getGene(i + 1)]);
			cost += dist;
		}
		setCost(cost);

	}

	@Override
	public void mutate() {
		final int length = getGenes().length;
		final int iswap1 = (int) (Math.random() * length);
		final int iswap2 = (int) (Math.random() * length);
		final Integer temp = getGene(iswap1);
		setGene(iswap1, getGene(iswap2));
		setGene(iswap2, temp);
		getGeneticAlgorithm().incrementMutation();
		
		
		//System.out.println("Mutation Calls : " + getGeneticAlgorithm().gettimesMutated());

	}

	/**
	 * Used to compare two chromosomes. Used to sort by cost.
	 * 
	 * @param other
	 *            The other chromosome to compare.
	 * @return The value 0 if the argument is a chromosome that has an equal
	 *         cost to this chromosome; a value less than 0 if the argument is a
	 *         chromosome with a cost greater than this chromosome; and a value
	 *         greater than 0 if the argument is a chromosome what a cost less
	 *         than this chromosome.
	 */
	@Override
	public int compareTo(Chromosome<Integer, TSPGeneticAlgorithm> other) {
		if (getCost() > other.getCost())
			return 1;
		else if (getCost() == other.getCost())
			return 0;
		else
			return -1;
	}

	/** @return the {@link #path} */
	public Waypoint[] getPath() {
		return path;
	}

	

}
