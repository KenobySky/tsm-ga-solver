
package net.JeffHeatonCode;

/**
 * Introduction to Neural Networks with Java, 2nd Edition
 * Copyright 2008 by Heaton Research, Inc.
 * http://www.heatonresearch.com/books/java-neural-2/
 *
 * ISBN13: 978-1-60439-008-7
 * ISBN:   1-60439-008-5
 *
 * This class is released under the:
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/copyleft/lesser.html
 */

import java.util.concurrent.Callable;

/** MateWorker: This class is used in conjunction with a thread pool. This allows the genetic algorithm to offload all of those
 * calculations to a thread pool.
 * 
 * @author Jeff Heaton
 * @version 2.1 */
public class MateWorker<CHROMOSOME_TYPE extends Chromosome<?, ?>> implements Callable<Integer> {

	private final CHROMOSOME_TYPE mother;
	private final CHROMOSOME_TYPE father;
	private final CHROMOSOME_TYPE child1;
	private final CHROMOSOME_TYPE child2;

	public MateWorker (final CHROMOSOME_TYPE mother, final CHROMOSOME_TYPE father, final CHROMOSOME_TYPE child1,
		final CHROMOSOME_TYPE child2) {
		this.mother = mother;
		this.father = father;
		this.child1 = child1;
		this.child2 = child2;
	}

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Integer call () throws Exception {
		this.mother.mate((Chromosome)this.father, (Chromosome)this.child1, (Chromosome)this.child2);
		return null;
	}

}
