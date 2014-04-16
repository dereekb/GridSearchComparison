package com.dereekb.gridsearch.algorithms.jps;

import com.dereekb.gridsearch.algorithms.components.AlgorithmListener;
import com.dereekb.gridsearch.models.Position;

public interface JumpPointSearchListener<T>
        extends AlgorithmListener<T> {

	public void checkingPosition(Position position);

	public void foundJumpPoint(Position position);

	public void failedFindingJumpPoint(Position neighbor);

}
