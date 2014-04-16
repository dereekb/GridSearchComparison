package com.dereekb.gridsearch.algorithms.graph;

import java.util.Collection;

import com.dereekb.gridsearch.algorithms.components.NodeExpander;
import com.dereekb.gridsearch.models.Position;

public interface GraphNodeExpander<T extends GraphNode<?>>
        extends NodeExpander<T> {

	public Collection<Position> expandablePositions(T node);

}
