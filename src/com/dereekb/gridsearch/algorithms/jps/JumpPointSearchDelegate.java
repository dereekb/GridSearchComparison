package com.dereekb.gridsearch.algorithms.jps;

import com.dereekb.gridsearch.algorithms.graph.GraphNode;
import com.dereekb.gridsearch.models.Position;

public interface JumpPointSearchDelegate<T extends GraphNode<?>> {

	public boolean isWalkablePosition(Position position);

	public T nodeForPosition(Position nextPosition);

}
