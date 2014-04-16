package com.dereekb.gridsearch.algorithms.graph;

import com.dereekb.gridsearch.algorithms.components.NodeParentAccessor;

public class GraphNodeLinkModifier<V>
        implements NodeParentAccessor<GraphNode<V>> {

	@Override
	public GraphNode<V> getParentNode(GraphNode<V> node) {
		return node.getParent();
	}

	@Override
	public void setParentNode(GraphNode<V> node,
	                          GraphNode<V> parent) {
		node.setParent(parent);
	}

}
