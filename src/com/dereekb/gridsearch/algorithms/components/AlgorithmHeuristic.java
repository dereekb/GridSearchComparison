package com.dereekb.gridsearch.algorithms.components;

/**
 * An algorithm herustic that returns weights in values of <V>.
 * 
 * @author dereekb
 * 
 * @param <T>
 *            Node Type
 * @param <V>
 *            herustic value type
 */
public interface AlgorithmHeuristic<T, V> {

	public V calculateHerusticValue(T current,
	                                T target);

	public String getName();

}
