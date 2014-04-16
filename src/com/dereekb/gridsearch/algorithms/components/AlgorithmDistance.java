package com.dereekb.gridsearch.algorithms.components;

public interface AlgorithmDistance<T, V> {

	/**
	 * Returns actual distance between two objects.
	 * 
	 * @return
	 */
	public V getDistance(T a,
	                     T b);

}
