package com.dereekb.gridsearch.algorithms.components;

import java.util.Comparator;
import java.util.List;

/**
 * Sorts the nodes in order to which they should be evauluated.
 * 
 * @author dereekb
 * 
 * @param <T>
 */
public interface NodeSorter<T>
        extends Comparator<T> {

	public List<T> sortNodes(Iterable<T> nodes);

}
