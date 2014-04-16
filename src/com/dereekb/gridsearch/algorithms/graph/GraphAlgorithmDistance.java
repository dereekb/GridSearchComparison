package com.dereekb.gridsearch.algorithms.graph;

import com.dereekb.gridsearch.algorithms.components.AlgorithmDistance;
import com.dereekb.gridsearch.models.Position;
import com.dereekb.gridsearch.models.Positionable;

public class GraphAlgorithmDistance<T extends Positionable>
        implements AlgorithmDistance<T, Double> {

	@Override
	public Double getDistance(T a,
	                          T b) {

		Position aPos = a.getPosition();
		Position bPos = b.getPosition();

		return aPos.distance(bPos);
	}

}
