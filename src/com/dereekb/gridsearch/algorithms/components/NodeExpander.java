package com.dereekb.gridsearch.algorithms.components;

import java.util.Collection;

/**
 * Responsible for expanding nodes for an algorithm.
 * 
 * @author dereekb
 */
public interface NodeExpander<T> {

	public Collection<T> expandNode(T node);

}
