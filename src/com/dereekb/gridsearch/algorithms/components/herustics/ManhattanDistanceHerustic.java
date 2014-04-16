package com.dereekb.gridsearch.algorithms.components.herustics;

import com.dereekb.gridsearch.algorithms.components.AlgorithmHeuristic;
import com.dereekb.gridsearch.models.Position;
import com.dereekb.gridsearch.models.Positionable;

public class ManhattanDistanceHerustic<T extends Positionable>
        implements AlgorithmHeuristic<T, Double> {

	@Override
	public Double calculateHerusticValue(Positionable current,
	                                     Positionable target) {
		Position currentPosition = current.getPosition();
		Position targetPosition = target.getPosition();
		Double distance = null;

		Integer xDifference = currentPosition.xDifference(targetPosition);
		Integer yDifference = currentPosition.yDifference(targetPosition);

		Integer manhattanDistance = xDifference + yDifference;
		distance = new Double(manhattanDistance);
		return distance;
	}

	@Override
	public String getName() {
		return "Manhattan";
	}

}
