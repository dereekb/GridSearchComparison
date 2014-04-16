package com.dereekb.gridsearch.models;

import com.dereekb.gridsearch.models.algorithm.GraphWorldNode;

public interface GraphWorldDelegate
        extends PositionExpanderDelegate {

	public GraphWorldNode createNodeForPosition(Position position);

}
