package com.dereekb.gridsearch.algorithms.components.herustics;

import com.dereekb.gridsearch.algorithms.components.AlgorithmHeuristic;
import com.dereekb.gridsearch.models.Position;
import com.dereekb.gridsearch.models.Positionable;

public class EuclidianDistanceHeuristic<T extends Positionable>
        implements AlgorithmHeuristic<T, Double> {

	@Override
	public Double calculateHerusticValue(Positionable current,
	                                     Positionable target) {
		Position currentPosition = current.getPosition();
		Position targetPosition = target.getPosition();
		Double distance = currentPosition.distance(targetPosition);
		return distance;
	}

	@Override
	public String getName() {
		return "Euclidian";
	}

}
