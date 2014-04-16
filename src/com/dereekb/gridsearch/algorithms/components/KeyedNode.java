package com.dereekb.gridsearch.algorithms.components;

/**
 * A keyed object.
 * 
 * @author dereekb
 * 
 */
public interface KeyedNode<V, K>
        extends Node<V> {

	public K getKey();

}
