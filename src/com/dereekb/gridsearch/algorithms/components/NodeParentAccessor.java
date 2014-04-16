package com.dereekb.gridsearch.algorithms.components;

/**
 * Modifies the links between nodes, for nodes.
 * 
 * @author dereekb
 * 
 */
public interface NodeParentAccessor<N> {

	public N getParentNode(N node);

	public void setParentNode(N node,
	                          N parent);

}
