package com.dereekb.gridsearch.models;

public interface PositionExpanderDelegate {

	public boolean isWalkablePosition(Position position);

	public boolean isBlockedPosition(Position position);

}
