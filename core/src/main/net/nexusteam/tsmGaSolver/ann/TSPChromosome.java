package net.nexusteam.tsmGaSolver.ann;

import net.JeffHeatonCode.Chromosome;
import net.JeffHeatonCode.NeuralNetworkError;

/** @author Mix Of Jeff Heaton Code with Our Team Code */
public class TSPChromosome extends Chromosome<Integer, TSPGeneticAlgorithm> {

	protected Waypoint[] path;

	public TSPChromosome(final TSPGeneticAlgorithm owner, final Waypoint path[]) {
		setGeneticAlgorithm(owner);
		this.path = path;

		final Integer genes[] = new Integer[this.path.length];
		final boolean taken[] = new boolean[path.length];

		for(int i = 0; i < genes.length; i++) {
			taken[i] = false;
		}
		for(int i = 0; i < genes.length - 1; i++) {
			int icandidate;

			do {
				icandidate = (int) (Math.random() * genes.length);
			} while(taken[icandidate]);

			genes[i] = icandidate;
			taken[icandidate] = true;
			if(i == genes.length - 2) {
				icandidate = 0;

				while(taken[icandidate]) {
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
		for(int i = 0; i < path.length - 1; i++) {
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
		getGeneticAlgorithm().incrementMutationCounter();

		boolean use2OptSwap = false;
		if(use2OptSwap)
		{
			setGenes(optimize(getGenes()));

		}

		// System.out.println("Mutation Calls : " + getGeneticAlgorithm().gettimesMutated());

	}

	//2Opt Methods
	private Waypoint[] optSwap(Integer genes[], int i, int k)
	{

		Integer auxGenes[] = new Integer[genes.length];
		//Step 1

		for(int index = 0; index < i - 1; index++)
		{
			auxGenes[index] = genes[index]; 
		}

		
		//Step 2
		int auxIndex = k;
		for(int index = i; index < k; index++)
		{
			auxGenes[index] = genes[auxIndex];
			auxIndex--;
		}

		//Step 3

		for(int index = k + 1; index < genes.length; i++)
		{
			auxGenes[index] = genes[i];
		}

		//2optSwap(route, i, k) {
		//     1. take route[0] to route[i-1] and add them in order to new_route
		//   2. take route[i] to route[k] and add them in reverse order to new_route
		//  3. take route[k+1] to end and add them in order to new_route
		//  return new_route;
		// }

		
		//example route: A ==> B ==> C ==> D ==> E ==> F ==> G ==> H ==> A
			//	   example i = 4, example k = 7
				//   new_route:
				  //     1. (A ==> B ==> C)
				    //   2. A ==> B ==> C ==> (G ==> F ==> E ==> D)
				      // 3. A ==> B ==> C ==> G ==> F ==> E ==> D (==> H ==> A)
		
		//

		
		//ROBIN IDK.
		//How do i do this?
		Waypoint[] auxPath = null;
 
		return auxPath;
	}

	/**
	 * @param genes
	 * @return
	 */
	private Integer[] optimize(Integer genes[])
	{
		boolean improved = false;
		double best_distance = Double.MAX_VALUE;

		while(!improved)
		{
			calculateCost();
			best_distance = getCost();

			for(int i = 0; i < getGenes().length; i++)
			{
				for(int k = i + 1; k < getGenes().length; k++)
				{

					TSPChromosome newRoute = new TSPChromosome(this.getGeneticAlgorithm(), optSwap(genes, i, k));

					newRoute.calculateCost();

					double new_distance = newRoute.getCost();

					if(new_distance < best_distance)
					{
						improved = true;
						genes = newRoute.getGenes();
					}

				}
			}
		}

		return genes;

		// repeat until no improvement is made {
		//     start_again:
		//   best_distance = calculateTotalDistance(existing_route)
		// for (i = 0; i < number of nodes eligible to be swapped - 1; i++) {
		//   for (k = i + 1; k < number of nodes eligible to be swapped; k++) {
		//     new_route = 2optSwap(existing_route, i, k)
		//   new_distance = calculateTotalDistance(new_route)
		// if (new_distance < best_distance) {
		//   existing_route = new_route
		// goto start_again
		//}
		//}
		//}
		//}

		//
	}

	/** Used to compare two chromosomes. Used to sort by cost.
	 * 
	 * @param other The other chromosome to compare.
	 * @return The value 0 if the argument is a chromosome that has an equal cost to this chromosome; a value less than 0 if the
	 *         argument is a chromosome with a cost greater than this chromosome; and a value greater than 0 if the argument is a
	 *         chromosome what a cost less than this chromosome. */
	@Override
	public int compareTo(Chromosome<Integer, TSPGeneticAlgorithm> other) {
		if(getCost() > other.getCost()) {
			return 1;
		} else if(getCost() == other.getCost())
			return 0;
		else {
			return -1;
		}
	}

	/** @return the {@link #path} */
	public Waypoint[] getPath() {
		return path;
	}

}
