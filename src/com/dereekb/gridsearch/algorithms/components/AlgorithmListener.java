package com.dereekb.gridsearch.algorithms.components;

import java.util.Collection;

/**
 * Listener that watches for when various algorithm events occur.
 * 
 * @author dereekb
 * 
 */
public interface AlgorithmListener<T> {

	/**
	 * Called when the algorithm starts.
	 * 
	 * Allows for listener setup/reset/etc.
	 * 
	 * @param start
	 * @param goal
	 */
	public void startedAlgorithm(T start,
	                             T goal);

	/**
	 * Called when the goal node is found.
	 * 
	 * @param node
	 */
	public void foundGoal(T node);

	/**
	 * Called when a node is checked.
	 * 
	 * @param node
	 */
	public void checkedNode(T node);

	/**
	 * Called when a node is expanded.
	 * 
	 * @param node
	 * @param children
	 */
	public void expandedNode(T node,
	                         Collection<T> children);

	/**
	 * Called when a node is removed and no longer needed.
	 * 
	 * @param node
	 */
	public void deprecatedNode(T node);

}
