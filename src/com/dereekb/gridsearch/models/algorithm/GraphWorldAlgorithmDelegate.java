package com.dereekb.gridsearch.models.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.dereekb.gridsearch.algorithms.components.GoalNodeChecker;
import com.dereekb.gridsearch.algorithms.components.NodeParentAccessor;
import com.dereekb.gridsearch.algorithms.graph.GraphNodeExpander;
import com.dereekb.gridsearch.algorithms.jps.JumpPointSearchDelegate;
import com.dereekb.gridsearch.models.GraphWorld;
import com.dereekb.gridsearch.models.GraphWorldDelegate;
import com.dereekb.gridsearch.models.Position;
import com.dereekb.gridsearch.models.PositionExpander;

public class GraphWorldAlgorithmDelegate
        implements NodeParentAccessor<GraphWorldNode>, GraphNodeExpander<GraphWorldNode>,
        GoalNodeChecker<GraphWorldNode>, JumpPointSearchDelegate<GraphWorldNode> {

	private GraphWorldDelegate delegate;
	private PositionExpander expander;

	public GraphWorldAlgorithmDelegate() {}

	public GraphWorldAlgorithmDelegate(GraphWorldDelegate delegate) {
		this.delegate = delegate;
	}

	// Goal Node Checker
	@Override
	public boolean isGoalNode(GraphWorldNode node,
	                          GraphWorldNode goal) {

		Position position = node.getPosition();
		Position goalPosition = goal.getPosition();
		return position.equals(goalPosition);
	}

	// Node Parent Accessor
	@Override
	public GraphWorldNode getParentNode(GraphWorldNode node) {
		GraphWorldNode parent = (GraphWorldNode) node.getParent();
		return parent;
	}

	@Override
	public void setParentNode(GraphWorldNode node,
	                          GraphWorldNode parent) {
		node.setParent(parent);
	}

	// Node Expander
	@Override
	public Collection<GraphWorldNode> expandNode(GraphWorldNode node) {
		List<GraphWorldNode> children = new ArrayList<GraphWorldNode>();

		Position position = node.getPosition();
		GraphWorld world = node.getValue();

		Iterable<Position> neighbors = this.expander.allAvailableNeighboringPositions(position);

		for (Position neighbor : neighbors) {
			GraphWorldNode child = new GraphWorldNode(world, neighbor);
			child.setParent(node);
			children.add(child);
		}

		return children;
	}

	@Override
	public Collection<Position> expandablePositions(GraphWorldNode node) {
		Position position = node.getPosition();
		Collection<Position> neighbors = this.expander.allAvailableNeighboringPositions(position);
		return neighbors;
	}

	public PositionExpander getExpander() {
		return expander;
	}

	public void setExpander(PositionExpander expander) {
		this.expander = expander;
	}

	@Override
	public boolean isWalkablePosition(Position position) {
		return delegate.isWalkablePosition(position);
	}

	@Override
	public GraphWorldNode nodeForPosition(Position position) {
		return delegate.createNodeForPosition(position);
	}

	public GraphWorldDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(GraphWorldDelegate delegate) {
		this.delegate = delegate;
	}
}
