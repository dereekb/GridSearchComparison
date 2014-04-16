package com.dereekb.gridsearch.models.algorithm;

import com.dereekb.gridsearch.algorithms.graph.GraphNode;
import com.dereekb.gridsearch.models.GraphWorld;
import com.dereekb.gridsearch.models.Position;

public class GraphWorldNode extends GraphNode<GraphWorld> {

	public GraphWorldNode(GraphWorld value, Position position) {
		super(value, position);
	}

}
