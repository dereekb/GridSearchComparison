package com.dereekb.gridsearch.algorithms.components;

/**
 * Graph searching algorithm that makes use of a herustic.
 * 
 * Has a listener that is accessed and called while the algorithm is running.
 * 
 * @author dereekb
 * 
 * @param <T>
 *            Node type.
 * @param <W>
 *            Weight type the herustic uses.
 */
public interface SearchAlgorithm<T, W> {

	public void setListener(AlgorithmListener<T> listener);

	public void setHerustic(AlgorithmHeuristic<T, W> herustic);

	public T search(T start,
	                T goal);

}
