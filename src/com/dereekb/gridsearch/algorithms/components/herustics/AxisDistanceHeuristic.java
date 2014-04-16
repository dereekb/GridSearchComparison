package com.dereekb.gridsearch.algorithms.components.herustics;

import com.dereekb.gridsearch.algorithms.components.AlgorithmHeuristic;
import com.dereekb.gridsearch.models.Position;
import com.dereekb.gridsearch.models.Positionable;

public class AxisDistanceHeuristic<T extends Positionable>
        implements AlgorithmHeuristic<T, Double> {

	private boolean xAxis = true;

	@Override
	public Double calculateHerusticValue(Positionable current,
	                                     Positionable target) {
		Position currentPosition = current.getPosition();
		Position targetPosition = target.getPosition();
		Double distance = null;

		if (xAxis) {
			Integer xDifference = Math.abs(currentPosition.getxPos() - targetPosition.getxPos());
			distance = new Double(xDifference);
		} else {
			Integer yDifference = Math.abs(currentPosition.getyPos() - targetPosition.getyPos());
			distance = new Double(yDifference);
		}

		return distance;
	}

	@Override
	public String getName() {
		return "Axis";
	}

}
